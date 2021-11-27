package com.letiencao.service;

public interface IVerifyCodeService {
	Long insertOne(String phoneNumber, String code);
	//BlocksModel findOne(Long idBlocks,Long idBlocked);
	boolean deleteVerifyCode(Long id);
}
