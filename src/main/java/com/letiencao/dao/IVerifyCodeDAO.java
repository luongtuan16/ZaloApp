package com.letiencao.dao;

import java.util.List;

import com.letiencao.model.VerifyCodeModel;

public interface IVerifyCodeDAO {
	Long insertOne(VerifyCodeModel verifyCodeModel);
	List<VerifyCodeModel> findByPhoneNumber(String phoneNumber);
	boolean deleteVerifyCode(Long id);
	boolean deleteByPhoneNumber(String phoneNumber);
}
