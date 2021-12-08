package com.letiencao.api.search;

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
import com.letiencao.model.SearchModel;
import com.letiencao.response.search.DataGetSavedSearchResp;
import com.letiencao.response.search.GetSavedSearchResp;
import com.letiencao.service.GenericService;
import com.letiencao.service.IAccountService;
import com.letiencao.service.ISearchService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.BaseService;
import com.letiencao.service.impl.SearchService;

@WebServlet("/api/get_saved_search")
public class GetSavedSearchAPI extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private GenericService genericService;
	private ISearchService searchService;

	public GetSavedSearchAPI() {
		genericService = new BaseService();
		accountService = new AccountService();
		searchService = new SearchService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		GetSavedSearchResp searchResponse = new GetSavedSearchResp();
		List<DataGetSavedSearchResp> listData = new ArrayList<DataGetSavedSearchResp>();

		String jwt = req.getParameter("token");
		String indexStr = req.getParameter("index");
		String countStr = req.getParameter("count");

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));

		if (accountModel.isActive()) {
			if (indexStr != null && countStr != null) {
				int index = Integer.valueOf(indexStr);
				int count = Integer.valueOf(countStr);
				List<SearchModel> listSearchs = searchService.findByAccountId(accountModel.getId());
				for (int i = index; i < listSearchs.size() && i < index + count; i++) {
					SearchModel searchModel = listSearchs.get(i);
					DataGetSavedSearchResp data = new DataGetSavedSearchResp();

					data.setId(searchModel.getId());
					data.setKeyword(searchModel.getKeyword());
					data.setCreated(searchModel.getCreatedDate());
					listData.add(data);

				}
				searchResponse.setData(listData);
				searchResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
				searchResponse.setMessage(BaseHTTP.MESSAGE_1000);

			} else {
				searchResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
				searchResponse.setMessage(BaseHTTP.MESSAGE_1004);

			}
		} else {
			searchResponse.setCode(String.valueOf(BaseHTTP.CODE_9995));
			searchResponse.setMessage(BaseHTTP.MESSAGE_9995);
		}

		resp.getWriter().print(gson.toJson(searchResponse));

	}
}
