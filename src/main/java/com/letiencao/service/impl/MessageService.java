package com.letiencao.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.letiencao.dao.IConversationDAO;
import com.letiencao.dao.IMessageDAO;
import com.letiencao.dao.impl.ConversationDAO;
import com.letiencao.dao.impl.MessageDAO;
import com.letiencao.model.ConversationModel;
import com.letiencao.model.MessageModel;
import com.letiencao.service.IMessageService;

public class MessageService implements IMessageService {
	private IMessageDAO messageDAO;
	private IConversationDAO conversationDAO;

	public MessageService() {
		messageDAO = new MessageDAO();
		conversationDAO = new ConversationDAO();
	}

	@Override
	public Long insertOne(MessageModel messageModel) {
		return messageDAO.insertOne(messageModel);
	}

	@Override
	public boolean deleteMessage(Long messageId) {
		return messageDAO.deleteMessage(messageId);
	}

	@Override
	public List<MessageModel> findConversation(Long conversationId) {
		return messageDAO.findConversation(conversationId);
	}

	@Override
	public boolean deleteConversation(Long conversationId) {
		return messageDAO.deleteConversation(conversationId);
	}

	@Override
	public MessageModel findOne(Long messId) {
		return messageDAO.findOne(messId);
	}

	@Override
	public MessageModel findLastMessage(Long conversationId) {
		List<MessageModel> messages = messageDAO.findConversation(conversationId);
		if (messages.size() > 0)
			return messages.get(0);
		return null;
	}

	@Override
	public List<MessageModel> searchMessage(String keyword, Long accountId) {
		List<MessageModel> messages = messageDAO.searchMessage(keyword);
		List<MessageModel> result = new ArrayList<MessageModel>();
		List<ConversationModel> conversations = conversationDAO.findListConversation(accountId);

		for (int i = 0; i < messages.size(); i++) {
			for (int j = 0; j < conversations.size(); j++)
				if (messages.get(i).getConversationId() == conversations.get(j).getId()) {
					result.add(messages.get(i));
					break;
				}
		}
		return result;
	}

}
