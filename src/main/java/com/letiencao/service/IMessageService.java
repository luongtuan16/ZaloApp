package com.letiencao.service;

import java.util.List;

import com.letiencao.model.MessageModel;

public interface IMessageService {
	Long insertOne(MessageModel messageModel);

	boolean deleteMessage(Long messageId);

	List<MessageModel> findConversation(Long conversationId);
	
	MessageModel findOne(Long messId);
	
	MessageModel findLastMessage(Long conversationId);
	
	boolean deleteConversation(Long conversationId);
	
	List<MessageModel> searchMessage(String keyword, Long accountId);
}
