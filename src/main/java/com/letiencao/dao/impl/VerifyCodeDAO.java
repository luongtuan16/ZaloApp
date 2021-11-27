package com.letiencao.dao.impl;

import com.letiencao.dao.IVerifyCodeDAO;
import com.letiencao.model.VerifyCodeModel;

public class VerifyCodeDAO extends BaseDAO<VerifyCodeModel> implements IVerifyCodeDAO {

	@Override
	public Long insertOne(VerifyCodeModel verifyCodeModel) {
		String sql = "INSERT INTO verify_code(deleted,createddate,phonenumber,vcode) VALUES(?,?,?,?)";
		return insertOne(sql, verifyCodeModel.isDeleted(),verifyCodeModel.getCreatedDate(),
				verifyCodeModel.getPhoneNumber(),verifyCodeModel.getCode());
	}

	@Override
	public VerifyCodeModel findByPhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteVerifyCode(Long id) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
