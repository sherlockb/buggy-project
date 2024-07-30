package com.spinifexit.controller;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spinifexit.exception.InvalidDbPropertiesException;
import com.spinifexit.exception.InvalidTenantIdExeption;
import com.spinifexit.model.Model;
import com.spinifexit.multitenant.manager.MultiTenantManager;
import com.spinifexit.multitenant.manager.TenantNotFoundException;
import com.spinifexit.multitenant.manager.TenantResolvingException;
import com.spinifexit.service.ModelService;

import java.sql.SQLException;

@Slf4j
@RestController
@RequestMapping("/models")
public class ModelController {
	
	private final Logger log = LoggerFactory.getLogger(ModelController.class);
	
	private static final String MSG_INVALID_TENANT_ID = "[!] DataSource not found for given tenant Id '{}'!";
	private static final String MSG_INVALID_DB_PROPERTIES_ID = "[!] DataSource properties related to the given tenant ('{}') is invalid!";
	private static final String MSG_RESOLVING_TENANT_ID = "[!] Could not resolve tenant ID '{}'!";

	private final ModelService modelService;
	private final MultiTenantManager tenantManager;
	
	@GetMapping
	public ResponseEntity<?> getAll(@RequestHeader(value = "X-TenantID") String tenantId) {
		setTenant(tenantId);
		return ResponseEntity.ok(modelService.findAll());
	}
	
	@PostMapping
	public ResponseEntity<?> create(@RequestHeader("X-TenantId") String tenantId) {
		log.info("[i] Received POST request for '{}'", tenantId);
		setTenant(tenantId);
		return ResponseEntity.ok(modelService.save(new Model(tenantId)));
	}

	private void setTenant(String tenantId) {
		try {
			tenantManager.setCurrentTenant("");
		} catch (SQLException e) {
			log.error(MSG_INVALID_DB_PROPERTIES_ID, tenantId);
			throw new InvalidDbPropertiesException();
		} catch (TenantNotFoundException e) {
			log.error(MSG_INVALID_TENANT_ID, tenantId);
			throw new InvalidTenantIdExeption();
		} catch (TenantResolvingException e) {
			log.error(MSG_RESOLVING_TENANT_ID, tenantId);
			throw new InvalidTenantIdExeption();
		}
	}
}