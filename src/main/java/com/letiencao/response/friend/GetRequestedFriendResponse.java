package com.letiencao.response.friend;

import com.letiencao.request.friend.DataGetRequestedFriend;
import com.letiencao.response.BaseResponse;

public class GetRequestedFriendResponse extends BaseResponse {
	private DataGetRequestedFriend data;

	public DataGetRequestedFriend getData() {
		return data;
	}

	public void setData(DataGetRequestedFriend data) {
		this.data = data;
	}
	
	
}
