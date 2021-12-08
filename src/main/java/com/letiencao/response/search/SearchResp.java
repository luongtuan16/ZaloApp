package com.letiencao.response.search;

import java.util.List;

import com.letiencao.response.BaseResponse;

public class SearchResp extends BaseResponse {
	private List<DataSearchResp> data;

	public List<DataSearchResp> getData() {
		return data;
	}

	public void setData(List<DataSearchResp> data) {
		this.data = data;
	}
	
}
