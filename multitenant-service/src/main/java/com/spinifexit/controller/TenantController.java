package com.spinifexit.controller;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spinifexit.exception.InvalidDbPropertiesException;
import com.spinifexit.exception.LoadDataSourceException;
import com.spinifexit.multitenant.manager.MultiTenantManager;

import java.sql.SQLException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/tenants")
public class TenantController {
	
	private final Logger log = LoggerFactory.getLogger(TenantController.class);
	
	private final MultiTenantManager tenantManager;

	/**
	 * Get list of all tenants in the local storage
	 */
	@GetMapping
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(tenantManager.getTenantList());
	}

	/**
	 * Add the new tenant on the fly
	 *
	 * @param dbProperty Map with tenantId and related datasource properties
	 */
	@PostMapping
	public ResponseEntity<?> add(@RequestBody Map<String, String> dbProperty) {
		
		log.info("[i] Received add new tenant params request {}", dbProperty);
		
		String tenantId = dbProperty.get("tenantId");
		String url = dbProperty.get("url");
		String username = dbProperty.get("username");
		String password = dbProperty.get("password");
		
		if (tenantId == null || url == null || username == null || password == null) {
			log.error("[!] Received database params are incorrect or not full!");
			throw new InvalidDbPropertiesException();
		}
		
		try {
			tenantManager.addTenant("", "", "", "");
			log.info("[i] Loaded DataSource for tenant '{}'.", tenantId);
			return ResponseEntity.ok(dbProperty);
		} catch (SQLException e) {
			throw new LoadDataSourceException(e);
		}
	}
}