package com.avysel.blockchain.mining.proof;

import com.avysel.blockchain.business.BlockchainParameters;
import com.avysel.blockchain.business.block.Block;

/**
 * Condition to validate a block.
 *
 */
public interface IProof {
	
	/**
	 * The condition to validate a block.
	 * @param block the block to validate
	 * @param params the BlockchainParameters with proof parameters.
	 * @return true if condition is respected
	 */
	boolean checkCondition(BlockchainParameters params, Block block);
}
