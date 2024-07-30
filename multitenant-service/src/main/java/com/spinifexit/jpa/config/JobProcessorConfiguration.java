package com.spinifexit.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.spinifexit.service.TenantConfigService;
import com.spinifexit.service.TenantConfigServiceImpl;


/**
 * This class allows us to define @Bean methods and is processed by the Spring container to generate bean definitions
 * and service requests for those beans at runtime
 *
 * @author cbalanza
 *
 */
@Configuration
@ComponentScan({ "com.spinifexit.service" })
public class JobProcessorConfiguration {

	@Bean
	public TenantConfigService tenantConfigService() {
		return new TenantConfigServiceImpl();
	}
}
