package com.letiencao.response.friend;

import com.letiencao.response.BaseResponse;

public class SetFriendResponse extends BaseResponse {
	private DataSetFriendResponse data;

	public DataSetFriendResponse getData() {
		return data;
	}

	public void setData(DataSetFriendResponse data) {
		this.data = data;
	}
}
