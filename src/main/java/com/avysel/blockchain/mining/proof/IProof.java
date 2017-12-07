package com.avysel.blockchain.mining.proof;

import com.avysel.blockchain.model.block.Block;

public interface IProof {
	public boolean checkCondition(Block block);
}
