package com.spinifexit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spinifexit.model.TenantConfig;

public interface TenantConfigRepo extends JpaRepository<TenantConfig, String>{

}
