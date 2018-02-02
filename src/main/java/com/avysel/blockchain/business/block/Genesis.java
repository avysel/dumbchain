package com.avysel.blockchain.business.block;

import com.avysel.blockchain.business.data.SingleData;

/**
 * The genesis Block.
 * The first Block to be added in a Chain.
 */
public class Genesis extends Block {
	
	/**
	 * Genesis index
	 */
	public static final long GENESIS_INDEX = 0;
	
	/**
	 * Genesis label data
	 */
	public static final String GENESIS_LABEL = "Genesis";
	
	/**
	 * Genesis hash
	 */
	public static final String GENESIS_HASH = "0072d9ff21065fd058e0e0e12f3ae9068593ca4220764557a7c36ccda9ef7017";
	
	/**
	 * Create the genesis block.
	 */
	public Genesis() {
		this.getBlockHeader().setIndex(Genesis.GENESIS_INDEX);
		this.getBlockHeader().setHash(Genesis.GENESIS_HASH);
		this.getBlockHeader().setPreviousHash(null);
		this.getBlockData().getDataList().add(new SingleData(Genesis.GENESIS_LABEL));
	}
	
	@Override
	public boolean isGenesis() {
		return true;
	}
}
