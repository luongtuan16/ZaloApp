package com.letiencao.service.impl;

import java.util.List;

import com.letiencao.dao.IConversationDAO;
import com.letiencao.dao.impl.ConversationDAO;
import com.letiencao.model.ConversationModel;
import com.letiencao.service.IConversationService;

public class ConversationService implements IConversationService {
	private IConversationDAO conversationDAO;

	public ConversationService() {
		conversationDAO = new ConversationDAO();
	}

	@Override
	public Long insertOne(ConversationModel conversationModel) {
		return conversationDAO.insertOne(conversationModel);
	}

	@Override
	public boolean deleteConversation(Long conversationId) {
		return conversationDAO.deleteConversation(conversationId);
	}

	@Override
	public ConversationModel findOne(Long conversationId) {
		return conversationDAO.findOne(conversationId);
	}

	@Override
	public ConversationModel findOne(Long idA, Long idB) {
		ConversationModel conversationModel = conversationDAO.findOne(idA, idB);
		if (conversationModel == null)
			return conversationDAO.findOne(idB, idA);
		return conversationModel;
	}

	@Override
	public List<ConversationModel> findListConversation(Long accountId) {
		return conversationDAO.findListConversation(accountId);
	}
}
