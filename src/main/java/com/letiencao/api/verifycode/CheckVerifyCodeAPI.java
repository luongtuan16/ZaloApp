package com.letiencao.api.verifycode;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.letiencao.api.BaseHTTP;
import com.letiencao.model.AccountModel;
import com.letiencao.model.VerifyCodeModel;
import com.letiencao.request.account.SignInRequest;
import com.letiencao.response.verifycode.CheckVerifyCodeResponse;
import com.letiencao.response.verifycode.DataCheckCodeResponse;
import com.letiencao.service.IAccountService;
import com.letiencao.service.IVerifyCodeService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.VerifyCodeService;

@WebServlet("/api/check_verify_code")
public class CheckVerifyCodeAPI extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private IVerifyCodeService verifyCodeService;

	public CheckVerifyCodeAPI() {

		accountService = new AccountService();
		verifyCodeService = new VerifyCodeService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		CheckVerifyCodeResponse checkVerifyCodeResponse = new CheckVerifyCodeResponse();
		DataCheckCodeResponse dataCheckCodeResponse = new DataCheckCodeResponse();
		String phoneNumber = req.getParameter("phonenumber");
		String code = req.getParameter("code_verify");

		if (code.length() == 4) {
			if (phoneNumber != null && phoneNumber.length() == 10 && phoneNumber.charAt(0) == '0') {
				AccountModel account = accountService.findByPhoneNumber(phoneNumber);
				if (account != null && !account.isActive()) {
					List<VerifyCodeModel> codeModels = verifyCodeService.findByPhoneNumber(phoneNumber);
					if (!codeModels.isEmpty()) {
						VerifyCodeModel verifyCodeModel = codeModels.get(0);

						if (verifyCodeModel.getCode().equals(code)) {
							//active account
							accountService.activeAccount(account.getId());
							
							//set response param
							SignInRequest signInRequest = new SignInRequest();
							signInRequest.setPhonenumber(phoneNumber);
							signInRequest.setPassword(account.getPassword());

							dataCheckCodeResponse.setToken(accountService.signIn(signInRequest));
							dataCheckCodeResponse.setActive(true);
							dataCheckCodeResponse.setId(account.getId());

							checkVerifyCodeResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
							checkVerifyCodeResponse.setMessage(BaseHTTP.MESSAGE_1000);
							checkVerifyCodeResponse.setData(dataCheckCodeResponse);
							
							//xoa code cu
							verifyCodeService.deleteVerifyCode(verifyCodeModel.getId());
						}
						else {
							checkVerifyCodeResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
							checkVerifyCodeResponse.setMessage(BaseHTTP.MESSAGE_1004);
						}
					} else {
						checkVerifyCodeResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
						checkVerifyCodeResponse.setMessage(BaseHTTP.MESSAGE_1004);
					}
				} else {
					checkVerifyCodeResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
					checkVerifyCodeResponse.setMessage(BaseHTTP.MESSAGE_1004);
				}
			} else {
				checkVerifyCodeResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
				checkVerifyCodeResponse.setMessage(BaseHTTP.MESSAGE_1004);
			}
		} else {
			// chua nhap code
			checkVerifyCodeResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
			checkVerifyCodeResponse.setMessage(BaseHTTP.MESSAGE_1002);
		}
		resp.getWriter().print(gson.toJson(checkVerifyCodeResponse));
	}
}
