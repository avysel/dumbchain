package com.avysel.blockchain.mining.proof;

import com.avysel.blockchain.model.block.Block;

public class ProofOfStake implements IProof {

	@Override
	public boolean checkCondition(Block block) {
		
		return false;
	}

}