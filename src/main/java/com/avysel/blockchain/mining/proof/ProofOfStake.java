package com.avysel.blockchain.mining.proof;

import com.avysel.blockchain.business.block.Block;

/**
 * Proof of stake condition to validate a block.
 */
public class ProofOfStake implements IProof {

	@Override
	public boolean checkCondition(Block block) {
		
		return false;
	}

}
