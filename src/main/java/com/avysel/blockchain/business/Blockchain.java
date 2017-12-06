package com.avysel.blockchain.business;

import com.avysel.blockchain.crypto.HashTools;
import com.avysel.blockchain.exception.ChainIntegrityException;
import com.avysel.blockchain.mining.Miner;
import com.avysel.blockchain.mining.PendingData;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.block.Genesis;
import com.avysel.blockchain.model.chain.Chain;
import com.avysel.blockchain.model.chain.ChainPart;
import com.avysel.blockchain.model.data.ISingleData;
import com.avysel.blockchain.network.NetworkManager;

/*
 * TODO verifier integrite d'un bloc a chaque lecture (HashTools.checkHash)
 * TODO verifier integrite de l'arbre a chaque initialisation
 *
 */

/**
 * The main class of the Blockchain.
 * Contains a unique @Chain of @Blocks and provides available operations on it.
 */
public class Blockchain {
	private Chain chain;
	private PendingData pendingData;
	private Miner miner;
	private NetworkManager network;
	
	private boolean mining = true;

	public Blockchain() {
		this.chain = new Chain();
		this.pendingData = new PendingData();
		this.miner = new Miner();	
		this.network = new NetworkManager(this);
	}
	
	public Blockchain(Chain chain) {
		this.chain = chain;
		this.pendingData = new PendingData();
		this.miner = new Miner();
		this.network = new NetworkManager(this);
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
		Block genesis = new Genesis();
		genesis.setTimestamp(System.currentTimeMillis());
		genesis.setHash(HashTools.calculateBlockHash(genesis));
		chain.linkBlock(genesis);
		genesis.setPreviousHash(null);
	}

	public long getLastIndex() {
		if(this.getChain().getLastBlock() != null) {
			return this.getChain().getLastBlock().getIndex();
		}
		else {
			return Genesis.GENESIS_INDEX;
		}
	}
	
	/**
	 * Display the @Chain from last @Block to Genesis @Block
	 */
	public void display() {
		Block currentBlock = chain.getLastBlock();
		
		while(currentBlock != null) {
			System.out.println(currentBlock);			
			currentBlock = BlockchainManager.findBlockByHash(chain, currentBlock.getPreviousHash());
		}
		
	}
	
	/**
	 * Load existing @Chain from database
	 */
	public void loadChain() {
		
	}
	

	
	/**
	 * Add a new data to be included in a block at one of the next mining.
	 * @param data
	 */
	public void addIncomingData(ISingleData data) {
		System.out.println("add data : "+data);
		pendingData.addData(data);
	}
	
	/**
	 * Start the @Blockchain.
	 */
	public void run() {
		
		System.out.println("Start miner.");
		while(isMining()) {
			Block block = miner.mine(pendingData);
			System.out.println("New block created with "+block.getDataList().size()+" data");
			chain.linkBlock(block);
			System.out.println(block);
			System.out.println("New block linked");
			System.out.println("Remaining : "+pendingData.size());
			if(pendingData.size() < 10) //TODO what to do when not enough data to find a block that respects condition ?
				setMining(false);
			
		}
		System.out.println("End miner.");
		System.out.println("Effort : "+chain.getEffort());
		
	}
	
	/**
	 *  Add a subchain to the end of the current @Blockchain.
	 *  Use this method when a subchain is obtained with mining or by the network, and is to be appended to the @Blockchain.
	 *  The index of subchain's first @Block must be current's chain last @Block +1.
	 * @param subChain
	 * @throws ChainIntegrityException 
	 */
	public void addSubChain(ChainPart subChain) throws ChainIntegrityException {
		this.getChain().addChainPart(subChain);
	}
	
	public void startNode() {
		network.start();
		//run();
	}
}
