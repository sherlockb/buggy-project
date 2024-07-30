package com.spinifexit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spinifexit.model.Model;

public interface ModelRepo extends JpaRepository<Model, String> {
}
