package com.letiencao.response.conversation;

import com.letiencao.response.BaseResponse;

public class GetConversationResp extends BaseResponse {
	private DataGetConversationResp data;

	public DataGetConversationResp getData() {
		return data;
	}

	public void setData(DataGetConversationResp data) {
		this.data = data;
	}
	
}
