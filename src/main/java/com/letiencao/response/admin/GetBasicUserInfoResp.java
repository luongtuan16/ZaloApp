package com.letiencao.response.admin;

import com.letiencao.response.BaseResponse;

public class GetBasicUserInfoResp extends BaseResponse {
	private DataGetBUIFResp data;

	public DataGetBUIFResp getData() {
		return data;
	}

	public void setData(DataGetBUIFResp data) {
		this.data = data;
	}
	
}
