package com.avysel.blockchain.model.chain;

import java.util.LinkedList;

import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.block.Genesis;

/**
 * The blockchain.
 * It contains the list of @Block, starting with a @Genesis
 */
public class Chain extends ChainPart {
	
	public Chain() {
		blockList = new LinkedList<Block>();
	}

	public Block getGenesis() {
		if(getFirstBlock().isGenesis())
			return getFirstBlock();
		else
			return null;
	}

	public void setGenesis(Block genesis) {
		blockList.addFirst(genesis);
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
		
		if( ! block.isGenesis()) {
			block.setPreviousHash(getLastBlock().getHash());
			block.setIndex(getLastIndex() + 1);
		}
		
		blockList.add(block);
	}
}
