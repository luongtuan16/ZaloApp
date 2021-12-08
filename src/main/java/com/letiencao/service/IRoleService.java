package com.letiencao.service;

import com.letiencao.model.RoleModel;

public interface IRoleService {
	Long findId(String role);
	RoleModel findById(Long id);
}
