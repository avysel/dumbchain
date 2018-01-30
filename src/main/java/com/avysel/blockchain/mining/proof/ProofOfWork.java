package com.avysel.blockchain.mining.proof;

import com.avysel.blockchain.model.block.Block;

public class ProofOfWork implements IProof {

	@Override
	public boolean checkCondition(Block block) {

		String hash = block.getHash();
		long difficulty = block.getDifficulty();

		return hash.startsWith("000") || difficulty > 1000000 && hash.startsWith("00") ;
	}

}
