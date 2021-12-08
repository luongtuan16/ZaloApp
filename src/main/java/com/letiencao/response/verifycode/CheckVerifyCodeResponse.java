package com.letiencao.response.verifycode;

import com.letiencao.response.BaseResponse;

public class CheckVerifyCodeResponse extends BaseResponse {
	private DataCheckCodeResponse data;

	public DataCheckCodeResponse getData() {
		return data;
	}

	public void setData(DataCheckCodeResponse data) {
		this.data = data;
	}
	
}
