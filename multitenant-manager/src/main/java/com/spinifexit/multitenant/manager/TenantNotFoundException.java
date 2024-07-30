package com.spinifexit.multitenant.manager;

public class TenantNotFoundException extends Exception {
	public TenantNotFoundException(String message) {
		super(message);
	}
}