package com.spinifexit.service;

import com.spinifexit.model.Model;

public interface ModelService {
	Iterable<Model> findAll();
	Model save(Model model);
}