package com.letiencao.dao.impl;

import com.letiencao.dao.IRoleDAO;
import com.letiencao.mapping.RoleMapping;
import com.letiencao.model.RoleModel;

public class RoleDAO extends BaseDAO<RoleModel> implements IRoleDAO{

	@Override
	public RoleModel findByRoleString(String role) {
		String sqlString = "SELECT * FROM role WHERE role = ?";
		return findOne(sqlString, new RoleMapping(), role);
	}

	@Override
	public RoleModel findbyId(Long id) {
		String sql = "SELECT * FROM role WHERE rolekey = ?";
		return findOne(sql, new RoleMapping(), id);
	}
	
}
