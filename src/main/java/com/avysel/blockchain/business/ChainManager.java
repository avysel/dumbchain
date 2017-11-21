package com.avysel.blockchain.business;

import java.util.List;

import com.avysel.blockchain.crypto.HashTools;
import com.avysel.blockchain.mining.Miner;
import com.avysel.blockchain.mining.PendingData;
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
	private PendingData pendingData;
	private Miner miner;
	
	private boolean mining = true;

	public ChainManager() {
		this.chain = new Chain();
		this.pendingData = new PendingData();
		this.miner = new Miner();		
	}
	
	public ChainManager(Chain chain) {
		this.chain = chain;
		this.pendingData = new PendingData();
		this.miner = new Miner();
	}
	
	public Chain getChain() {
		return chain;
	}
	
	public boolean isMining() {
		return mining;
	}

	public void setMining(boolean mining) {
		this.mining = mining;
	}

	/**
	 * Find a @Block with a given height
	 * @param height the height
	 * @return the @Block with the given height
	 */
	public Block findBlockByHeight(long height) {// TODO index au lieu de height
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
		Block genesis = new Block();
		genesis.addData(new SingleData("Genesis"));
		genesis.setIndex(Chain.GENESIS_INDEX);
		genesis.setTimestamp(System.currentTimeMillis());
		genesis.setPreviousHash(null);
		genesis.setHash(HashTools.calculateBlockHash(genesis));
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
		boolean integrity = true;
		for(Block block : blockList) {
			if(!checkBlockHash(block)) {
				System.out.println("Bad hash for block "+block.getIndex());
				integrity = false;
			}
			if(!checkBlockPrevious(block)) {
				System.out.println("Bad previous for block "+block.getIndex());
				integrity = false;
			}
		}
		return integrity;
	}
	
	/**
	 * Perform integrity check for a @Block. Only checks @Block's hash
	 * @param block the @Block to check
	 * @return true if @Block's hash is the one expected
	 */
	public boolean checkBlockHash(Block block) {
		String hash = HashTools.calculateBlockHash(block);
		System.out.println("Check hash, expected : "+hash+", found : "+block.getHash());
		return hash.equals(block.getHash());
	}
	
	/**
	 * Perform integrity check for a @Block. Checks if @Block's parent is the one expected.
	 * @param block the @Block to check
	 * @return if @Block's parent is the one expected
	 */
	public boolean checkBlockPrevious(Block block) {
		Block previous = findBlockByHash(block.getPreviousHash());
		return block.isGenesis() || (previous != null && previous.getIndex() == block.getIndex() -1);
	}
	
	/**
	 * Add a new data to be included in a block at one of the next mining.
	 * @param data
	 */
	public void addIncomingData(SingleData data) {
		System.out.println("add data : "+data);
		pendingData.addData(data);
	}
	
	public void run() {
		
		System.out.println("Start miner.");
		while(isMining()) {
			Block block = miner.mine(pendingData);
			System.out.println("New block created");
			chain.linkBlock(block);
			System.out.println("New block linked");
			
			if(pendingData.size() < 10)
				setMining(false);
			
		}
		System.out.println("End miner.");
		
	}
}
