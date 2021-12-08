package com.letiencao.api.search;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.letiencao.api.BaseHTTP;
import com.letiencao.model.AccountModel;
import com.letiencao.model.SearchModel;
import com.letiencao.response.BaseResponse;
import com.letiencao.service.GenericService;
import com.letiencao.service.IAccountService;
import com.letiencao.service.ISearchService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.BaseService;
import com.letiencao.service.impl.SearchService;

@WebServlet("/api/del_saved_search")
public class DelSavedSearchAPI extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private GenericService genericService;
	private ISearchService searchService;

	public DelSavedSearchAPI() {
		genericService = new BaseService();
		accountService = new AccountService();
		searchService = new SearchService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		BaseResponse baseResponse = new BaseResponse();

		String jwt = req.getParameter("token");
		String searchIdStr = req.getParameter("search_id");
		String allStr = req.getParameter("all");

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));

		if (accountModel.isActive()) {
			if (allStr != null) {
				int all = Integer.valueOf(allStr);
				if (all == 1) {
					boolean t = searchService.deleteByAccountId(accountModel.getId());
					if (t) {
						baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
						baseResponse.setMessage(BaseHTTP.MESSAGE_1000);
					} else {
						baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9999));
						baseResponse.setMessage(BaseHTTP.MESSAGE_9999);
					}
					resp.getWriter().print(gson.toJson(baseResponse));
					return;
				} else if (all != 0) {
					baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
					baseResponse.setMessage(BaseHTTP.MESSAGE_1004);
					resp.getWriter().print(gson.toJson(baseResponse));
					return;
				}

			}
			if (searchIdStr != null) {
				Long searchId = Long.valueOf(searchIdStr);
				SearchModel searchModel = searchService.findOne(searchId, accountModel.getId());
				if (searchModel != null) {
					boolean t = searchService.deleteById(searchId);
					if (t) {
						baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
						baseResponse.setMessage(BaseHTTP.MESSAGE_1000);
					} else {
						baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9999));
						baseResponse.setMessage(BaseHTTP.MESSAGE_9999);
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
			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9995));
			baseResponse.setMessage(BaseHTTP.MESSAGE_9995);
		}

		resp.getWriter().print(gson.toJson(baseResponse));
	}
}
