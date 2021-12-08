package com.letiencao.api.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.letiencao.service.IAccountService;
import com.letiencao.service.IVerifyCodeService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.VerifyCodeService;

public class GetAdminPermissionAPI extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private IVerifyCodeService verifyCodeService;

	public GetAdminPermissionAPI() {

		accountService = new AccountService();
		verifyCodeService = new VerifyCodeService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
	}
}
