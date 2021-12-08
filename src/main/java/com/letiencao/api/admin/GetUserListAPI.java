package com.letiencao.api.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.letiencao.api.BaseHTTP;
import com.letiencao.model.AccountModel;
import com.letiencao.response.admin.DataGerUserListResp;
import com.letiencao.response.admin.GetUserListResp;
import com.letiencao.service.GenericService;
import com.letiencao.service.IAccountService;
import com.letiencao.service.IRoleService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.BaseService;
import com.letiencao.service.impl.RoleService;

@WebServlet("/api/get_user_list")
public class GetUserListAPI extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IAccountService accountService;
	private GenericService genericService;
	private IRoleService roleService;

	public GetUserListAPI() {
		genericService = new BaseService();
		accountService = new AccountService();
		roleService = new RoleService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		GetUserListResp response = new GetUserListResp();
		List<DataGerUserListResp> listData = new ArrayList<DataGerUserListResp>();

		String jwt = req.getParameter("token");
		String indexStr = req.getParameter("index");
		String countStr = req.getParameter("count");

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));

		int index = 0, count = 20;
		if (indexStr != null)
			index = Integer.valueOf(indexStr);
		if (countStr != null)
			count = Integer.valueOf(countStr);
		if (index >= 0 && count >= 0) {
			if (accountModel.getRoleId() != null && accountModel.getRoleId() > roleService.findId("user")) {
				List<AccountModel> users = accountService.findAll();
				for (int i = index; i < users.size() && i < index + count; i++) {
					if (accountModel.getRoleId() >= users.get(i).getRoleId()) {
						DataGerUserListResp data = new DataGerUserListResp();
						data.setUser_id(users.get(i).getId());
						data.setUsername(users.get(i).getName());
						data.setIs_active(users.get(i).isActive());
						data.setAvatar(users.get(i).getAvatar());
						data.setLastLogin(null);

						listData.add(data);
					}
				}
				response.setData(listData);
				response.setCode(String.valueOf(BaseHTTP.CODE_1000));
				response.setMessage(BaseHTTP.MESSAGE_1000);
			} else {
				response.setCode(String.valueOf(BaseHTTP.CODE_1009));
				response.setMessage(BaseHTTP.MESSAGE_1009);
			}
		} else {
			response.setCode(String.valueOf(BaseHTTP.CODE_1004));
			response.setMessage(BaseHTTP.MESSAGE_1004);
		}

		resp.getWriter().print(gson.toJson(response));
	}

}
