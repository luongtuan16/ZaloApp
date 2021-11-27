package com.letiencao.api.verifycode;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.letiencao.api.BaseHTTP;
import com.letiencao.model.AccountModel;
import com.letiencao.response.BaseResponse;
import com.letiencao.service.IAccountService;
import com.letiencao.service.IVerifyCodeService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.VerifyCodeService;

@WebServlet("/api/get_verify_code")
public class GetVerifyCodeAPI extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private IVerifyCodeService verifyCodeService;

	public GetVerifyCodeAPI() {
		accountService = new AccountService();
		verifyCodeService = new VerifyCodeService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		BaseResponse baseResponse = new BaseResponse();
		String phoneNumber = req.getParameter("phonenumber");
		if (phoneNumber != null && phoneNumber.length() == 10 && phoneNumber.charAt(0) == '0') {
			AccountModel user = accountService.findByPhoneNumber(phoneNumber);
			if (user != null) {
				Random random = new Random();
				int code = random.nextInt(8999) + 1000;
				verifyCodeService.insertOne(phoneNumber, String.valueOf(code));
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
				baseResponse.setMessage(BaseHTTP.MESSAGE_1000);
			} else {
				// user chua dang ky
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
				baseResponse.setMessage(BaseHTTP.MESSAGE_1004);
			}
		} else {
			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
			baseResponse.setMessage(BaseHTTP.MESSAGE_1004);
		}
		resp.getWriter().print(gson.toJson(baseResponse));
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
