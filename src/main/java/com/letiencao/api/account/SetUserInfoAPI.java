package com.letiencao.api.account;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.letiencao.model.AccountModel;
import com.letiencao.request.account.SetUserInfoRequest;
import com.letiencao.response.account.SetUserInfoResponse;
import com.letiencao.service.GenericService;
import com.letiencao.service.IAccountService;
import com.letiencao.service.IBlocksService;
import com.letiencao.service.IFriendService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.BaseService;
import com.letiencao.service.impl.BlocksService;
import com.letiencao.service.impl.FriendService;

@WebServlet("/api/set-user-info")
public class SetUserInfoAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IAccountService accountService;
	private IBlocksService blocksService;
	private GenericService genericService;
	private IFriendService friendService;

	public SetUserInfoAPI() {
		// TODO Auto-generated constructor stub
		accountService = new AccountService();
		blocksService = new BlocksService();
		genericService = new BaseService();
		friendService = new FriendService();
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
		SetUserInfoRequest setUserInfoRequest = gson.fromJson(request.getReader(), SetUserInfoRequest.class);
		SetUserInfoResponse setUserInfoResponse = new SetUserInfoResponse();
		String jwt = request.getParameter("token");
		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
		
	}
}
