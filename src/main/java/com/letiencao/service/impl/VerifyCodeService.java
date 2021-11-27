package com.letiencao.service.impl;

import java.sql.Timestamp;

import com.letiencao.dao.IVerifyCodeDAO;
import com.letiencao.dao.impl.VerifyCodeDAO;
import com.letiencao.model.VerifyCodeModel;
import com.letiencao.service.IVerifyCodeService;

public class VerifyCodeService implements IVerifyCodeService {
	
	private IVerifyCodeDAO verifyCodeDAO;
	
	public VerifyCodeService() {
		verifyCodeDAO = new VerifyCodeDAO();
	}
	@Override
	public Long insertOne(String phoneNumber, String code) {
		VerifyCodeModel verifyCodeModel = new VerifyCodeModel();
		verifyCodeModel.setCode(code);
		verifyCodeModel.setPhoneNumber(phoneNumber);
		verifyCodeModel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		verifyCodeModel.setDeleted(false);
		return verifyCodeDAO.insertOne(verifyCodeModel);
	}

	@Override
	public boolean deleteVerifyCode(Long id) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
