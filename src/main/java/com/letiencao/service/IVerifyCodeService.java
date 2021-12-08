package com.letiencao.service;

import java.util.List;

import com.letiencao.model.VerifyCodeModel;

public interface IVerifyCodeService {
	Long insertOne(String phoneNumber, String code);
	List<VerifyCodeModel> findByPhoneNumber(String phoneNumber);
	boolean deleteVerifyCode(Long id);
	boolean deleteByPhoneNumber(String phoneNumber);
}
