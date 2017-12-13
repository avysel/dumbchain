package com.avysel.blockchain.business;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.avysel.blockchain.crypto.HashTools;
import com.avysel.blockchain.exception.ChainIntegrityException;
import com.avysel.blockchain.mining.Miner;
import com.avysel.blockchain.mining.DataPool;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.block.Genesis;
import com.avysel.blockchain.model.chain.Chain;
import com.avysel.blockchain.model.chain.ChainPart;
import com.avysel.blockchain.model.data.SingleData;
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

	private static Logger log = Logger.getLogger("com.avysel.blockchain.business.Blockchain");
	
	private String nodeId;
	
	// the list of blocks
	private Chain chain;

	// storage for data waiting to be included in a block
	private DataPool dataPool;

	// block generator
	private Miner miner;

	// data network input/output
	private NetworkManager network;

	public Blockchain() {
		this.nodeId = UUID.randomUUID().toString();
		this.chain = new Chain();
		createChain();
		this.dataPool = new DataPool();
		this.miner = new Miner(this, dataPool);	
		this.network = new NetworkManager(this);
	}

	public Blockchain(Chain chain) {
		this.chain = chain;
		createChain();
		this.dataPool = new DataPool();
		this.miner = new Miner(this, dataPool);
		this.network = new NetworkManager(this);
	}
	
	/**
	 * Returns the unique ID of the current Blockchain node.
	 * @return unique ID of this node
	 */
	public String getNodeId() {
		return nodeId;
	}

	public Chain getChain() {
		return chain;
	}

	/**
	 * Create the @Chain and set a genesis @Block
	 */
	private void createChain() {
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

	/**
	 * Gets the Block index of last Block linked at the end of the Chain
	 * @return the last Block index
	 */
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

	public DataPool getDataPool() {
		return this.getDataPool();
	}

	/**
	 * Add a new data to be included in a block at one of the next mining.
	 * @param data
	 * @throws InterruptedException 
	 */
	public void addIncomingData(SingleData data) throws InterruptedException {
		dataPool.addData(data);
	}

	/**
	 * Add a block to the blockchain.
	 * Link the new block to the existing chain and send this block to the network.
	 * @param block the block to add
	 */
	public void addBlock(Block block) {
		
		// link block to current chain
		getChain().linkBlock(block);
		
		// send block to the network
		network.sendBlock(block);
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

	/**
	 * Starts the Blockchain (network listening and mining)
	 */
	public void start() {
		log.info("Starting blockchain");
		network.start();
		miner.start();
	}

	/**
	 * Stops the Blockchain (network listening and mining)
	 */
	public void stop() {
		log.info("Stopping blockchain");
		network.stop();
		miner.stop();
	}
}
