package com.avysel.blockchain.business;

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

	public void setChain(Chain chain) {
		this.chain = chain;
	}

	/**
	 * 	Create a new @Block from given data
	 * @return the new @Block
	 */
	public Block createBlock(/* data */) {
	
		return new Block();
		
	}
	
	/**
	 * Add a new @Block to the @Chain
	 * @param block the @Block to add
	 */
	public void linkBlock(Block block) {
		
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
		return new Block();
	}
	
	/**
	 * Create the genesis @Block and add it to the @Chain
	 */
	public void createGenesis() {
		
	}

}
