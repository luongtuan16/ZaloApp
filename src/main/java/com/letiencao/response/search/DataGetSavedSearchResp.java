package com.letiencao.response.search;

import java.sql.Timestamp;

public class DataGetSavedSearchResp {
	private Long id;
	private String keyword;
	private Timestamp created;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	
}
