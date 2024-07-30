package com.spinifexit;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.spinifexit.model.TenantConfig;
import com.spinifexit.multitenant.manager.MultiTenantManager;
import com.spinifexit.service.TenantConfigService;
import com.spinifexit.utils.ObjUtils;

import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;

@Slf4j
@EntityScan("com.spinifexit.model")
@EnableJpaRepositories(basePackages = "com.spinifexit.repo")
@SpringBootApplication
public class Application {
	
	private final Logger log = LoggerFactory.getLogger(Application.class);
	
	private static final Logger logStatic = LoggerFactory.getLogger(Application.class);
	
	private final MultiTenantManager tenantManager;
	
	private static TenantConfigService tenantConfigService;
	
	public Application(MultiTenantManager tenantManager, TenantConfigService tenantConfigService) {
		this.tenantManager = tenantManager;
		Application.tenantConfigService = tenantConfigService;
		this.tenantManager.setTenantResolver(Application::tenantResolver);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * Load tenant datasource properties from the folder 'tenants/onStartUp`
	 * when the app has started.
	 */
	@EventListener
	public void onReady(ApplicationReadyEvent event) {
		
		String masterHostUrl = ObjUtils.getEnvironmentVariable("MASTER_HOST_URL");

		String dbName = ObjUtils.getEnvironmentVariable("RDS_DB_NAME");
		String userName = ObjUtils.getEnvironmentVariable("RDS_USERNAME");
		String password = ObjUtils.getEnvironmentVariable("RDS_PASSWORD");
		String hostname = ObjUtils.getEnvironmentVariable("RDS_HOSTNAME");
		String port = ObjUtils.getEnvironmentVariable("RDS_PORT");
		String jdbcParams = ObjUtils.getEnvironmentVariable("RDS_JDBC_PARAMS");
		String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName
				+ "?useUnicode=yes&characterEncoding=UTF-8";

		if (ObjUtils.hasValue(jdbcParams)) {
			jdbcUrl += "&" + jdbcParams;
		}		

		if (!ObjUtils.hasValue(masterHostUrl) 
				|| !ObjUtils.hasValue(dbName) 
				|| !ObjUtils.hasValue(userName) 
				|| !ObjUtils.hasValue(password) 
				|| !ObjUtils.hasValue(jdbcUrl)) {
			log.warn("[!] Tenant not found at onStartUp");
			return;
		}

		try {
			tenantManager.addTenant(masterHostUrl, jdbcUrl, userName, password);
			log.info("[i] Loaded DataSource for tenant '{}'.", masterHostUrl);
		} catch (SQLException e) {
			log.error(format("[!] Could not load DataSource for tenant '%s'!", masterHostUrl), e);
		}
	}

	/**
	 * Example of the tenant resolver - load the given tenant datasource properties
	 * from the folder 'tenants/atRuntime'
	 *
	 * @param tenantId tenant id
	 * @return tenant DataSource
	 */
	private static DataSourceProperties tenantResolver(String tenantUrl) {
		
		//load tenant connections from the database
		List<TenantConfig> lstTenantConfig = tenantConfigService.findAll();

		if (lstTenantConfig == null) {
			String msg = "[!] Tenant not found at runtime!";
			logStatic.error(msg);
			throw new RuntimeException(msg);
		}
		
		for(TenantConfig tenantConfig: lstTenantConfig) {
			
			if(tenantConfig != null) {
			
				String dbName = tenantConfig.getSchemaName();
				String userName = tenantConfig.getDbConnection().getDbUsername();
				String password = tenantConfig.getDbConnection().getDbPassword();
				String hostname = tenantConfig.getDbConnection().getDbUrl();
				String port = tenantConfig.getDbConnection().getPort();
				String jdbcParams = ObjUtils.getEnvironmentVariable("RDS_JDBC_PARAMS");
				String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName
						+ "?useUnicode=yes&characterEncoding=UTF-8";
	
				// for certain cases where we need to add extra parameters in the JDBC url
				// (eg. setting SQL_SAFE_UPDATES in the sessionVariables for db migration)
				// @see
				// https://stackoverflow.com/questions/26425045/how-to-pass-session-variables-in-jdbc-url-properly
				if (ObjUtils.hasValue(jdbcParams)) {
					jdbcUrl += "&" + jdbcParams;
				}
				
				if (tenantUrl.equals(tenantConfig.getTenantUrl())) {
					DataSourceProperties properties = new DataSourceProperties();
					properties.setUrl(jdbcUrl);
					properties.setUsername(userName);
					properties.setPassword(password);
					return properties;
				}
				
				
			}
			
		}
		
		String msg = "[!] Given tenant not found!";
		logStatic.error(msg);
		throw new RuntimeException(msg);
	}
}
