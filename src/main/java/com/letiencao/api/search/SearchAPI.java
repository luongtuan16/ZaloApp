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
import com.letiencao.model.ConversationModel;
import com.letiencao.model.MessageModel;
import com.letiencao.model.SearchModel;
import com.letiencao.response.search.DataSearchResp;
import com.letiencao.response.search.SearchResp;
import com.letiencao.service.GenericService;
import com.letiencao.service.IAccountService;
import com.letiencao.service.IConversationService;
import com.letiencao.service.IMessageService;
import com.letiencao.service.ISearchService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.BaseService;
import com.letiencao.service.impl.ConversationService;
import com.letiencao.service.impl.MessageService;
import com.letiencao.service.impl.SearchService;

@WebServlet("/api/search")
public class SearchAPI extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private GenericService genericService;
	private ISearchService searchService;
	private IMessageService messageService;
	private IConversationService conversationService;

	public SearchAPI() {
		genericService = new BaseService();
		accountService = new AccountService();
		searchService = new SearchService();
		messageService = new MessageService();
		conversationService = new ConversationService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		SearchResp searchResp = new SearchResp();
		List<DataSearchResp> data = new ArrayList<DataSearchResp>();

		String jwt = req.getParameter("token");
		// subjectId = 0: find message and account
		// = 1: find message
		// = 2: find account
		String subjectStr = req.getParameter("subject");
		String detail = req.getParameter("details");

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
		if (accountModel.isActive()) {
			if (subjectStr != null && detail != null) {
				Long subjectId = Long.valueOf(subjectStr);
				detail = standardizeUserName(detail);
				if (subjectId == 0L || subjectId == 1) {
					// find message
					List<MessageModel> messages = messageService.searchMessage(detail, accountModel.getId());
					for (int i = 0; i < messages.size(); i++) {
						ConversationModel conversation = conversationService
								.findOne(messages.get(i).getConversationId());
						if (conversation != null) {
							DataSearchResp dataSearchResp = new DataSearchResp();
							AccountModel user = null;
							if (conversation.getAccountA() == accountModel.getId())
								user = accountService.findById(conversation.getAccountB());
							else
								user = accountService.findById(conversation.getAccountA());
							if (user != null) {
								dataSearchResp.setUserId(user.getId());
								dataSearchResp.setAvatar(user.getAvatar());
								dataSearchResp.setUserName(user.getName());
								dataSearchResp.setMessage(messages.get(i).getContent());

								data.add(dataSearchResp);
							}
						}
					}
				}
				if (subjectId == 0L || subjectId == 2L) {
					// find account
					List<AccountModel> accounts = accountService.searchAccount(detail);
					for (int i = 0; i < accounts.size(); i++) {
						DataSearchResp dataSearchResp = new DataSearchResp();
						dataSearchResp.setUserId(accounts.get(i).getId());
						dataSearchResp.setAvatar(accounts.get(i).getAvatar());
						dataSearchResp.setUserName(accounts.get(i).getName());

						data.add(dataSearchResp);
					}
				}

				// insert search-info into db
				SearchModel searchModel = new SearchModel();
				searchModel.setDeleted(false);
				searchModel.setAccountId(accountModel.getId());
				searchModel.setCreatedBy(accountModel.getPhoneNumber());
				searchModel.setKeyword(detail);
				Long t = searchService.insertOne(searchModel);
				if (t >= 0) {
					searchResp.setCode(String.valueOf(BaseHTTP.CODE_1000));
					searchResp.setMessage(BaseHTTP.MESSAGE_1000);
					searchResp.setData(data);
				} else {
					searchResp.setCode(String.valueOf(BaseHTTP.CODE_1001));
					searchResp.setMessage(BaseHTTP.MESSAGE_1001);
				}

			} else {
				searchResp.setCode(String.valueOf(BaseHTTP.CODE_1002));
				searchResp.setMessage(BaseHTTP.MESSAGE_1002);
			}
		} else {
			searchResp.setCode(String.valueOf(BaseHTTP.CODE_9995));
			searchResp.setMessage(BaseHTTP.MESSAGE_9995);
		}

		resp.getWriter().print(gson.toJson(searchResp));
	}

	public String standardizeUserName(String str) {
		str = str.trim();
		while (str.indexOf("  ") != -1)
			str = str.replaceAll("  ", " ");
		return str;
	}
}
