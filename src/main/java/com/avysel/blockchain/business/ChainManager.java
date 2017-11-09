package com.avysel.blockchain.business;

import com.avysel.blockchain.crypto.HashTools;
import com.avysel.blockchain.model.Block;
import com.avysel.blockchain.model.BlockData;
import com.avysel.blockchain.model.BlockHeader;
import com.avysel.blockchain.model.Chain;

public class ChainManager {
	private Chain chain;

	public ChainManager() {
		
	}
	
	public ChainManager(Chain chain) {
		this.chain = chain;
	}
	
	public Chain getChain() {
		return chain;
	}

	/**
	 * 	Create a new @Block from given data
	 * @return the new @Block
	 */
	public Block createBlock(String data) {
			
		BlockHeader blockHeader = new BlockHeader();
		BlockData blockData = new BlockData(data);
				
		blockHeader.setTimestamp(System.currentTimeMillis());
				
		Block block = new Block(blockHeader, blockData);
		blockHeader.setHash(HashTools.calculateBlockHash(block));
		
		return block;
	}
	
	/**
	 * Find a @Block with a given height
	 * @param height the height
	 * @return the @Block with the given height
	 */
	public Block findBlockByHeight(long height) {
		return new Block();
	}
	
	/**
	 * Find a @Block with a given hash
	 * @param hash the hash
	 * @return the @Block with the given hash
	 */
	public Block findBlockByHash(String hash) {
		
		if(hash == null) return null;
		
		for(Block block : chain.getBlockList()){
			if(block.getBlockHeader().getHash() != null 
				&& block.getBlockHeader().getHash().equals(hash))
				return block;
		}
		return null;
	}
	
	/**
	 * Create the @Chain and set a genesis @Block
	 */
	public void createChain() {
		chain = new Chain();
		createGenesis();
	}
	
	/**
	 * Create the genesis @Block and add it to the @Chain
	 */
	private void createGenesis() {
		Block genesis = createBlock("Genesis");
		chain.linkBlock(genesis);
		genesis.getBlockHeader().setPreviousHash(null);
	}

	public void display() {
		Block currentBlock = chain.getLastBlock();
		
		while(currentBlock != null) {
			System.out.println(currentBlock.getBlockData().getData().toString());
			currentBlock = findBlockByHash(currentBlock.getBlockHeader().getPreviousHash());
		}
		
	}
}
