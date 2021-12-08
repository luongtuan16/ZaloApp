package com.letiencao.service.impl;

import java.sql.Timestamp;

import com.letiencao.dao.IAccountDAO;
import com.letiencao.dao.IBlockUserDAO;
import com.letiencao.dao.impl.AccountDAO;
import com.letiencao.dao.impl.BlockUserDAO;
import com.letiencao.model.BlocksModel;
import com.letiencao.service.IBlockUserService;

public class BlockUserService implements IBlockUserService{
	private IBlockUserDAO blockUserDAO;
	private IAccountDAO accountDAO;
	public BlockUserService() {
		blockUserDAO = new  BlockUserDAO();
		accountDAO = new AccountDAO();
	}
	@Override
	public Long insertOne(Long idBlocks, Long idBlocked) {
		BlocksModel blocksModel = new BlocksModel();
		String phoneNumber = accountDAO.findById(idBlocks).getPhoneNumber();
		blocksModel.setCreatedBy(phoneNumber);
		blocksModel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		blocksModel.setDeleted(false);
		blocksModel.setIdBlocked(idBlocked);
		blocksModel.setIdBlocks(idBlocks);
		return blockUserDAO.insertOne(blocksModel);
	}

	@Override
	public BlocksModel findOne(Long idBlocks, Long idBlocked) {
		return blockUserDAO.findOne(idBlocks,idBlocked);
	}

	@Override
	public boolean deleteBlock(Long id) {
		return blockUserDAO.deleteBlock(id);
	}
	@Override
	public boolean deleteUserBlocks(Long accountId) {
		return blockUserDAO.deleteUserBlocks(accountId);
	}
	
}
