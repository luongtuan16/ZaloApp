package com.letiencao.api.conversation;

import java.io.IOException;

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
import com.letiencao.response.BaseResponse;
import com.letiencao.service.GenericService;
import com.letiencao.service.IAccountService;
import com.letiencao.service.IConversationService;
import com.letiencao.service.IMessageService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.BaseService;
import com.letiencao.service.impl.ConversationService;
import com.letiencao.service.impl.MessageService;

@WebServlet("/api/delete_message")
public class DeleteMessageAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private GenericService genericService;
	private IConversationService conversationService;
	private IMessageService messageService;

	public DeleteMessageAPI() {
		accountService = new AccountService();
		genericService = new BaseService();
		conversationService = new ConversationService();
		messageService = new MessageService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		BaseResponse baseResponse = new BaseResponse();

		String jwt = req.getParameter("token");
		String messIdStr = req.getParameter("message_id");
		// String conversationIdStr = req.getParameter("conversation_id");
		// String partnerIdStr = req.getParameter("partner_id");

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
		if (accountModel != null && accountModel.isActive()) {
			if (messIdStr != null) {
				Long messId = Long.valueOf(messIdStr);
				MessageModel messageModel = messageService.findOne(messId);
				if (messageModel != null) {
					ConversationModel conversationModel = conversationService.findOne(messageModel.getConversationId());
					if (conversationModel != null && (conversationModel.getAccountA() == accountModel.getId()
							|| conversationModel.getAccountB() == accountModel.getId())) {
						boolean t = messageService.deleteMessage(messId);
						if (t) {
							baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
							baseResponse.setMessage(BaseHTTP.MESSAGE_1000);
						} else {
							baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1005));
							baseResponse.setMessage(BaseHTTP.MESSAGE_1005);
						}
					} else {
						baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1009));
						baseResponse.setMessage(BaseHTTP.MESSAGE_1009);
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
