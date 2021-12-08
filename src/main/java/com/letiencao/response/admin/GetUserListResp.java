package com.letiencao.response.admin;

import java.util.List;

import com.letiencao.response.BaseResponse;

public class GetUserListResp extends BaseResponse {
	private List<DataGerUserListResp> data;

	public List<DataGerUserListResp> getData() {
		return data;
	}

	public void setData(List<DataGerUserListResp> data) {
		this.data = data;
	}

}
