package com.spinifexit.multitenant.manager;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.spinifexit.utils.ObjUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.lang.String.format;

@Slf4j
@Configuration
public class MultiTenantManager {
	
	private final Logger log = LoggerFactory.getLogger(MultiTenantManager.class);

	private final ThreadLocal<String> currentTenant = new ThreadLocal<>();
	private final Map<Object, Object> tenantDataSources = new ConcurrentHashMap<>();
	private final DataSourceProperties properties;

	private Function<String, DataSourceProperties> tenantResolver;

	private AbstractRoutingDataSource multiTenantDataSource;

	public MultiTenantManager(DataSourceProperties properties) {
		this.properties = properties;
	}
	
	public MultiTenantManager() {
		this.properties = new DataSourceProperties();
	}	

	@Bean
	public DataSource dataSource() {

		multiTenantDataSource = new AbstractRoutingDataSource() {
			@Override
			protected Object determineCurrentLookupKey() {
				return currentTenant.get();
			}
		};
		multiTenantDataSource.setTargetDataSources(tenantDataSources);
		multiTenantDataSource.setDefaultTargetDataSource(defaultDataSource());
		multiTenantDataSource.afterPropertiesSet();
		return multiTenantDataSource;
	}

	public void setTenantResolver(Function<String, DataSourceProperties> tenantResolver) {
		this.tenantResolver = tenantResolver;
	}

	public void setCurrentTenant(String tenantId) throws SQLException, TenantNotFoundException, TenantResolvingException {
		if (tenantIsAbsent(tenantId)) {
			if (tenantResolver != null) {
				DataSourceProperties properties;
				try {
					properties = tenantResolver.apply(tenantId);
					log.debug("[d] Datasource properties resolved for tenant ID '{}'", tenantId);
				} catch (Exception e) {
					throw new TenantResolvingException(e, "Could not resolve the tenant!");
				}

				String url = properties.getUrl();
				String username = properties.getUsername();
				String password = properties.getPassword();

				addTenant(tenantId, url, username, password);
			} else {
				throw new TenantNotFoundException(format("Tenant %s not found!", tenantId));
			}
		}
		currentTenant.set(tenantId);
		log.debug("[d] Tenant '{}' set as current.", tenantId);
	}

	public void addTenant(String tenantId, String url, String username, String password) throws SQLException {

		DataSource dataSource = DataSourceBuilder.create()
				.driverClassName(properties.getDriverClassName())
				.url(url)
				.username(username)
				.password(password)
				.build();

		// Check that new connection is 'live'. If not - throw exception
		try(Connection c = dataSource.getConnection()) {
			tenantDataSources.put(tenantId, dataSource);
			multiTenantDataSource.afterPropertiesSet();
			log.debug("[d] Tenant '{}' added.", tenantId);
		}
	}

	public DataSource removeTenant(String tenantId) {
		Object removedDataSource = tenantDataSources.remove(tenantId);
		multiTenantDataSource.afterPropertiesSet();
		return (DataSource) removedDataSource;
	}

	public boolean tenantIsAbsent(String tenantId) {
		return !tenantDataSources.containsKey(tenantId);
	}

	public Collection<Object> getTenantList() {
		return tenantDataSources.keySet();
	}

	private DriverManagerDataSource defaultDataSource() {
		
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
		DriverManagerDataSource defaultDataSource = new DriverManagerDataSource();
		defaultDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		defaultDataSource.setUrl(jdbcUrl);
		defaultDataSource.setUsername(userName);
		defaultDataSource.setPassword(password);
		return defaultDataSource;
	}
}
