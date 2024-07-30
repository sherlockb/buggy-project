package com.spinifexit.multitenant.manager;

public class TenantResolvingException extends Exception {
	public TenantResolvingException(Throwable throwable, String message) {
		super(message, throwable);
	}
}
