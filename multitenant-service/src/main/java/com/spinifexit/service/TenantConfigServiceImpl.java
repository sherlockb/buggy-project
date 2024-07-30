package com.spinifexit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spinifexit.model.TenantConfig;
import com.spinifexit.repo.TenantConfigRepo;

@Service
public class TenantConfigServiceImpl implements TenantConfigService {
	
	@Autowired
	private TenantConfigRepo tenantConfigRepo;
	
	@Override
	public List<TenantConfig> findAll() {
		return tenantConfigRepo.findAll();
	}

}
