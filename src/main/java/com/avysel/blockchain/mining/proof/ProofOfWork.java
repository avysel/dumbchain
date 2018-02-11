package com.avysel.blockchain.mining.proof;

import com.avysel.blockchain.business.block.Block;

/**
 * Proof of stake condition to validate a block.
 */
public class ProofOfWork implements IProof {

	@Override
	public boolean checkCondition(Block block) {

		String hash = block.getHash();
		long difficulty = block.getDifficulty();

		return hash.startsWith("0000") || difficulty > 1000000 && hash.startsWith("000");
	}

}
