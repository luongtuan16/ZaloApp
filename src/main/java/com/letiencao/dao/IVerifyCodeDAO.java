package com.letiencao.dao;

import com.letiencao.model.VerifyCodeModel;

public interface IVerifyCodeDAO {
	Long insertOne(VerifyCodeModel verifyCodeModel);
	VerifyCodeModel findByPhoneNumber(String phoneNumber);
	boolean deleteVerifyCode(Long id);
}
