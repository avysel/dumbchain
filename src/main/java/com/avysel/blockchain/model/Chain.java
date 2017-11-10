package com.avysel.blockchain.model;

import java.util.ArrayList;

import com.avysel.blockchain.model.Block;

public class Chain {
	private Block genesis;

	private Block lastBlock;

	private ArrayList<Block> blockList;

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

	public Block getLastBlock() {
		return lastBlock;
	}

	public void setLastBlock(Block lastBlock) {
		this.lastBlock = lastBlock;
	}

	/**
	 * Add a new @Block to the @Chain, and set @Block link data (previous hash ...)
	 * @param block the @Block to add
	 */
	public void linkBlock(Block block) {
		if(this.lastBlock != null) {
			block.setPreviousHash(this.lastBlock.getHash());
			block.setIndex(this.lastBlock.getIndex() + 1);
		}
		else {
			block.setPreviousHash(null);
			block.setIndex(1);			
		}

		blockList.add(block);
		lastBlock = block;
	}

	public ArrayList<Block> getBlockList() {
		return blockList;
	}

	public void setBlockList(ArrayList<Block> blockList) {
		this.blockList = blockList;
	}
}
