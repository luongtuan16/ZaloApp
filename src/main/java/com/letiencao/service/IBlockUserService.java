package com.letiencao.service;

import com.letiencao.model.BlocksModel;

public interface IBlockUserService {
	Long insertOne(Long idBlocks,Long idBlocked);
	BlocksModel findOne(Long idBlocks,Long idBlocked);
	boolean deleteBlock(Long id);
}
