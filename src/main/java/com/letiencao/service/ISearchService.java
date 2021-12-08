package com.letiencao.service;

import java.util.List;

import com.letiencao.model.SearchModel;

public interface ISearchService {
	Long insertOne(SearchModel searchModel);
	
	List<SearchModel> findByAccountId(Long accountId);
	
	boolean deleteByAccountId(Long accountId);
	
	boolean deleteById(Long searchId);
	
	SearchModel findOne(Long searchId, Long accountId);
}
