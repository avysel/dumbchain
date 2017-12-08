package com.avysel.blockchain.model.block;

import com.avysel.blockchain.model.data.SingleData;

/**
 * The genesis @Block
 * The first @Block to be added in a @Chain.
 */
public class Genesis extends Block {
	
	public static final long GENESIS_INDEX = 0;
	public static final String GENESIS_LABEL = "Genesis";
	
	public Genesis() {
		this.getBlockHeader().setIndex(Genesis.GENESIS_INDEX);
		this.getBlockHeader().setPreviousHash(null);
		this.getBlockData().getDataList().add(new SingleData(Genesis.GENESIS_LABEL));
	}
	
	@Override
	public boolean isGenesis() {
		return true;
	}
}
