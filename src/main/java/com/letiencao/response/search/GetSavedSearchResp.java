package com.letiencao.response.search;

import java.util.List;

import com.letiencao.response.BaseResponse;

public class GetSavedSearchResp extends BaseResponse{
	private List<DataGetSavedSearchResp> data;

	public List<DataGetSavedSearchResp> getData() {
		return data;
	}

	public void setData(List<DataGetSavedSearchResp> data) {
		this.data = data;
	}
	
}
