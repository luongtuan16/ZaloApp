package com.letiencao.service.impl;

import com.letiencao.dao.IRoleDAO;
import com.letiencao.dao.impl.RoleDAO;
import com.letiencao.model.RoleModel;
import com.letiencao.service.IRoleService;

public class RoleService implements IRoleService {
	private IRoleDAO roleDAO;
	
	public RoleService() {
		roleDAO = new RoleDAO();
	}
	@Override
	public Long findId(String role) {
		RoleModel roleModel = roleDAO.findByRoleString(role);
		if (roleModel != null)
			return roleModel.getId();
		return -1L;
	}
	@Override
	public RoleModel findById(Long id) {
		return roleDAO.findbyId(id);
	}

}
