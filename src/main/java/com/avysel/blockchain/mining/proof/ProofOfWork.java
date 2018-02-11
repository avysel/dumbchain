package com.avysel.blockchain.mining.proof;

import com.avysel.blockchain.business.BlockchainParameters;
import com.avysel.blockchain.business.block.Block;

/**
 * Proof of stake condition to validate a block.
 */
public class ProofOfWork implements IProof {

	@Override
	public boolean checkCondition(BlockchainParameters params, Block block) {

		String hash = block.getHash();
		long difficulty = block.getDifficulty();

		return hash.startsWith(params.getProperties().getProofOfWorkPrefix()) 
				|| difficulty > params.getProperties().getFallbackProofOfWorkStep() 
				&& hash.startsWith(params.getProperties().getFallbackProofOfWorkPrefix());
	}

}
