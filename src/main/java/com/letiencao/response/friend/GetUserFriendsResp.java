package com.letiencao.response.friend;

import com.letiencao.response.BaseResponse;

public class GetUserFriendsResp extends BaseResponse {
	private DataGetUserFriendsResp data;

	public DataGetUserFriendsResp getData() {
		return data;
	}

	public void setData(DataGetUserFriendsResp data) {
		this.data = data;
	}
	
}
