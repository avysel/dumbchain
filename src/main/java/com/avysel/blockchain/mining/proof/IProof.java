package com.avysel.blockchain.mining.proof;

import com.avysel.blockchain.model.block.Block;

/**
 * Condition to validate a block.
 *
 */
public interface IProof {
	
	/**
	 * The condition to validate a block.
	 * @param block the block to validate
	 * @return true if condition is respected
	 */
	boolean checkCondition(Block block);
}
