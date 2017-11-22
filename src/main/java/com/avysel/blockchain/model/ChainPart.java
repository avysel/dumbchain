package com.avysel.blockchain.model;

import java.util.ArrayList;

/**
 * A part of blockchain.
 * It contains the list of @Block, but contains no @Genesis. The @ChainPart is to be appended to an existing @Chain
 */
public class ChainPart {
	protected ArrayList<Block> blockList;
	protected Block lastBlock;
	
	public ArrayList<Block> getBlockList() {
		return blockList;
	}

	public void setBlockList(ArrayList<Block> blockList) {
		this.blockList = blockList;
	}
	
	public Block getLastBlock() {
		return lastBlock;
	}

	public void setLastBlock(Block lastBlock) {
		this.lastBlock = lastBlock;
	}	

	/**
	 * Returns the index of last @Block added to the @ChainPart
	 * @return the index of last @Block if exists, -1 otherwise.
	 */
	public long getLastIndex() {
		if(this.getLastBlock() != null) {
			return this.getLastBlock().getIndex();
		}
		else {
			return -1;
		}
	}	
	
	/**
	 * Add a new @Block to the @Chain, and set @Block link data (previous hash ...). It does not manage the @Genesis @Block.
	 * @param block the @Block to add
	 */
	public void linkBlock(Block block) {
		
		if(! block.isGenesis()) {
			block.setPreviousHash(this.lastBlock.getHash());
			block.setIndex(getLastIndex() + 1);
			blockList.add(block);
			lastBlock = block;
		}
	}
	
	public void addChainPart(ChainPart chainPart) {
		
	}
}
