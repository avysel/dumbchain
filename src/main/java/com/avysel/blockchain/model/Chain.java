package com.avysel.blockchain.model;

import java.util.ArrayList;

import com.avysel.blockchain.model.Block;

/**
 * The blockchain.
 * It contains the list of @Block, starting with a @Genesis
 */
public class Chain extends ChainPart {
	private Block genesis;
	
	public Chain() {
		blockList = new ArrayList<Block>();
	}

	public Block getGenesis() {
		return genesis;
	}

	public void setGenesis(Block genesis) {
		this.genesis = genesis;
		blockList.add(genesis);
	}

	@Override
	public long getLastIndex() {
		if(this.getLastBlock() != null) {
			return this.getLastBlock().getIndex();
		}
		else {
			return Genesis.GENESIS_INDEX;
		}
	}	
	
	/**
	 * Add a new @Block to the @Chain, and set @Block link data (previous hash ...). It also manages to add a @Block if it is the @Genesis @Block
	 * @param block the @Block to add.
	 */
	@Override
	public void linkBlock(Block block) {
		
		if(block.isGenesis()) {
			genesis = block;
		}
		else {
			block.setPreviousHash(this.lastBlock.getHash());
			block.setIndex(getLastIndex() + 1);
		}
		
		blockList.add(block);
		lastBlock = block;
	}
}
