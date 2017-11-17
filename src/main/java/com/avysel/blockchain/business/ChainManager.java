package com.avysel.blockchain.business;

import java.util.Arrays;
import java.util.List;

import com.avysel.blockchain.crypto.HashTools;
import com.avysel.blockchain.model.Block;
import com.avysel.blockchain.model.Chain;
import com.avysel.blockchain.model.SingleData;

/*
 * TODO verifier integrite d'un bloc a chaque lecture (HashTools.checkHash)
 * TODO verifier integrite de l'arbre a chaque initialisation
 *
 */

/**
 * Contains a unique @Chain of @Blocks and provides available operations on it.
 *
 */
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
	public Block createBlock(List<String> dataList) {
			
		Block block = new Block();
		block.setTimestamp(System.currentTimeMillis());	
		
		long lastIndex = getLastIndex();
		if( lastIndex == Chain.GENESIS_INDEX)
			block.setIndex(Chain.GENESIS_INDEX);
		else
			block.setIndex(getLastIndex() + 1);
		
		for(String data : dataList) {
			block.addData(new SingleData(data));
		}		
		
		block.setHash(HashTools.calculateBlockHash(block));
		
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
			if(block.getHash() != null 
				&& block.getHash().equals(hash))
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
		Block genesis = createBlock(Arrays.asList("Genesis"));
		chain.linkBlock(genesis);
		genesis.setPreviousHash(null);
	}

	public long getLastIndex() {
		if(this.getChain().getLastBlock() != null) {
			return this.getChain().getLastBlock().getIndex();
		}
		else {
			return Chain.GENESIS_INDEX;
		}
	}
	
	/**
	 * Display the @Chain from last @Block to Genesis @Block
	 */
	public void display() {
		Block currentBlock = chain.getLastBlock();
		
		while(currentBlock != null) {
			System.out.println(currentBlock);			
			currentBlock = findBlockByHash(currentBlock.getPreviousHash());
		}
		
	}
	
	/**
	 * Load existing @Chain from database
	 */
	public void loadChain() {
		
	}
	
	/**
	 * Perform integrity check for the @Chain
	 */
	public boolean checkChain() {
		List<Block> blockList = chain.getBlockList();
		for(Block block : blockList) {
			if(!checkBlockHash(block)) {
				System.out.println("Bad hash for block "+block.getIndex());
				return false;
			}
			if(!checkBlockPrevious(block)) {
				System.out.println("Bad previous for block "+block.getIndex());
				return false;
			}
		}
		return true;
	}
	
	/**
	 *  Perform integrity check for a @Block. (Only checks @Block's hash)
	 * @param block the @Block to check
	 * @return true if @Block integrity is good
	 */
	public boolean checkBlockHash(Block block) {
		String hash = HashTools.calculateBlockHash(block);	
		return hash.equals(block.getHash());
	}
	
	public boolean checkBlockPrevious(Block block) {
		Block previous = findBlockByHash(block.getPreviousHash());
		return block.isGenesis() || (previous != null && previous.getIndex() == block.getIndex() +1);
	}
}
