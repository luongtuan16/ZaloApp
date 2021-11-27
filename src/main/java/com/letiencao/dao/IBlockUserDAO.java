package com.letiencao.dao;

import com.letiencao.model.BlocksModel;

public interface IBlockUserDAO {
	Long insertOne(BlocksModel blocksModel);
	BlocksModel findOne(Long idBlocks, Long idBlocked);
	boolean deleteBlock(Long id);
}
