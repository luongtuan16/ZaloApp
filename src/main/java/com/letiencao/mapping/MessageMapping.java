package com.letiencao.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.letiencao.model.MessageModel;

public class MessageMapping implements IRowMapping<MessageModel> {

	@Override
	public MessageModel mapRow(ResultSet resultSet) {
		try {
			MessageModel model = new MessageModel();
			model.setId(resultSet.getLong("id"));
			model.setConversationId(resultSet.getLong("conversationid"));
			model.setCreatedDate(resultSet.getTimestamp("createddate"));
			model.setContent(resultSet.getString("content"));
			model.setaToB(resultSet.getBoolean("atob"));
			model.setUnread(resultSet.getBoolean("unread"));
			return model;
		} catch (SQLException e) {
			System.out.println("Failed Row Mapping Message Mapping : " + e.getMessage());
			return null;
		}
	}

}
