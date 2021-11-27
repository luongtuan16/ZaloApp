package com.letiencao.response.account;

import com.letiencao.response.BaseResponse;

public class SetUserInfoResponse extends BaseResponse{
	private DataSetUserInfoResponse data;

	public DataSetUserInfoResponse getData() {
		return data;
	}

	public void setData(DataSetUserInfoResponse data) {
		this.data = data;
	}
	
}
