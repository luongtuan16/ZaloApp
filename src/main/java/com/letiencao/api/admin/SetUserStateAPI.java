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
import com.letiencao.response.BaseResponse;
import com.letiencao.service.GenericService;
import com.letiencao.service.IAccountService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.BaseService;


@WebServlet("/api/set_user_state")
public class SetUserStateAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IAccountService accountService;

	private GenericService genericService;

	public SetUserStateAPI() {

		accountService = new AccountService();

		genericService = new BaseService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		BaseResponse baseResponse = new BaseResponse();

		String jwt = req.getParameter("token");
		String userIdQuery = req.getParameter("user_id");
		String stateQuery = req.getParameter("state");
		// String roleKeyQuery = req.getParameter("role_key");

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));

		if (userIdQuery != null && stateQuery != null) {
			Long stateLong = Long.valueOf(stateQuery);
			boolean state = false;
			if (stateLong == 1L)
				state = true;
			Long userId = Long.valueOf(userIdQuery);
			AccountModel user = accountService.findById(userId);
			if (user != null) {
				if (user.getRoleId() != null && user.getRoleId() >= accountModel.getRoleId()) {
					baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1009));
					baseResponse.setMessage(BaseHTTP.MESSAGE_1009);
					resp.getWriter().print(gson.toJson(baseResponse));
				} else {
					if (user.isActive() != state) {
						boolean t = false;
						if (state)
							t = accountService.activeAccount(userId);
						else
							t = accountService.deactiveAccount(userId);
						if (t) {
							baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
							baseResponse.setMessage(BaseHTTP.MESSAGE_1000);
						} else {
							baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9999));
							baseResponse.setMessage(BaseHTTP.MESSAGE_9999);
						}

					} else {
						baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1010));
						baseResponse.setMessage(BaseHTTP.MESSAGE_1010);
					}
				}

			} else {
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
				baseResponse.setMessage(BaseHTTP.MESSAGE_1004);
			}
		} else {
			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
			baseResponse.setMessage(BaseHTTP.MESSAGE_1002);
		}
		resp.getWriter().print(gson.toJson(baseResponse));
	}
}
