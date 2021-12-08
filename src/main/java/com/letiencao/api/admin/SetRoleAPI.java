package com.letiencao.api.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.letiencao.api.BaseHTTP;
import com.letiencao.model.AccountModel;
import com.letiencao.model.RoleModel;
import com.letiencao.response.BaseResponse;
import com.letiencao.service.GenericService;
import com.letiencao.service.IAccountService;
import com.letiencao.service.IRoleService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.BaseService;
import com.letiencao.service.impl.RoleService;

@WebServlet("/api/set_role")
public class SetRoleAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private GenericService genericService;
	private IRoleService roleService;

	public SetRoleAPI() {

		accountService = new AccountService();
		genericService = new BaseService();
		roleService = new RoleService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		BaseResponse baseResponse = new BaseResponse();

		String jwt = req.getParameter("token");
		String userIdStr = req.getParameter("user_id");
		String roleStr = req.getParameter("role");

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
		if (accountModel != null) {
			RoleModel roleModel = roleService.findById(accountModel.getRoleId());
			if (roleModel != null && roleModel.getRole().equalsIgnoreCase("superadmin")) {
				if (userIdStr!=null) {
					Long userId = Long.valueOf(userIdStr);
					AccountModel admin = accountService.findById(userId);
					if (admin != null && admin.getId() != accountModel.getId()) {
						Long roleId = -1L;
						if (roleStr!=null) {
							roleId = roleService.findId(roleStr);
							if (roleId == admin.getRoleId()) {
								baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1010));
								baseResponse.setMessage(BaseHTTP.MESSAGE_1010);
								resp.getWriter().print(gson.toJson(baseResponse));
								return;
							}
						} else {
							roleId = roleService.findId("user");
							// t = accountService.setRole(userId, "user");
						}
						boolean t = accountService.setRole(userId, roleId);
						if (t) {
							baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
							baseResponse.setMessage(BaseHTTP.MESSAGE_1000);
						} else {
							baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9995));
							baseResponse.setMessage(BaseHTTP.MESSAGE_9995);
						}
					} else {
						baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
						baseResponse.setMessage(BaseHTTP.MESSAGE_1004);
					}
				} else {
					baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
					baseResponse.setMessage(BaseHTTP.MESSAGE_1002);
				}
			} else {
				// khong co quyen
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9997));
				baseResponse.setMessage(BaseHTTP.MESSAGE_9997);
			}
		} else {
			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9996));
			baseResponse.setMessage(BaseHTTP.MESSAGE_9996);
		}
		resp.getWriter().print(gson.toJson(baseResponse));
	}
}
