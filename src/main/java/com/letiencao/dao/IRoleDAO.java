package com.letiencao.dao;

import com.letiencao.model.RoleModel;

public interface IRoleDAO {
	RoleModel findByRoleString(String role);
	RoleModel findbyId(Long id);
}
