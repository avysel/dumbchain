package com.avysel.blockchain.business;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.chainbuilder.ChainCatchUpBuilder;
import com.avysel.blockchain.business.chainbuilder.ChainConsensusBuilder;
import com.avysel.blockchain.business.chainbuilder.ChainConsensusBuilder.RejectReason;
import com.avysel.blockchain.business.chainbuilder.ChainSender;
import com.avysel.blockchain.exception.BlockIntegrityException;
import com.avysel.blockchain.exception.ChainIntegrityException;
import com.avysel.blockchain.mining.DataPool;
import com.avysel.blockchain.mining.Miner;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.block.Genesis;
import com.avysel.blockchain.model.chain.Chain;
import com.avysel.blockchain.model.chain.ChainPart;
import com.avysel.blockchain.model.data.ISingleData;
import com.avysel.blockchain.model.data.SingleData;
import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.data.message.NetworkMessage;
import com.avysel.blockchain.network.peer.Peer;

/**
 * The main class of the Blockchain.
 * Contains a unique @Chain of @Blocks and provides available operations on it.
 */
public class Blockchain {

	public static final boolean MINING = true;
	public static final boolean NOT_MINING = false;

	private static Logger log = Logger.getLogger("com.avysel.blockchain.business.Blockchain");


	// is the current blockchain mining ? (default true, else only building chain by listening)
	private boolean mining;

	// unique identifier of blockchain node
	private String nodeId;

	// the list of blocks
	private Chain chain;

	// storage for data waiting to be included in a block
	private DataPool dataPool;

	// block generator
	private Miner miner;

	// data network input/output
	private NetworkManager network;

	// build chain according to consensus rules
	private ChainConsensusBuilder consensusBuilder;

	// to catch-up with existing chain
	private ChainCatchUpBuilder catchUpBuilder;

	// is catch-up completed (true) or still in progress (false)
	private boolean catchUpCompleted = false;

	public Blockchain() {
		this.mining = true;
		init();
	}

	public Blockchain(boolean mining) {
		this.mining = mining;
		init();
	}

	private void init() {
		this.nodeId = UUID.randomUUID().toString();
		this.chain = new Chain();
		createChain();
		this.dataPool = new DataPool();
		this.miner = new Miner(this, dataPool);
		this.network = new NetworkManager(this);
		this.consensusBuilder = new ChainConsensusBuilder(this);		
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

	public void setInitialChain(Chain chain) {
		this.chain = chain;
	}

	public boolean isMining() {
		return mining;
	}

	public void setMining(boolean mining) {
		if(this.mining != mining) {
			this.mining = mining;
			if(mining)
				miner.start();
			else
				miner.stop();
		}
	}	

	/**
	 * Create the genesis @Block and add it to the @Chain
	 */
	private void createGenesis() {
		Block genesis = new Genesis();
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
		return this.dataPool;
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
	 * Add a new block to be linked at the end of the chain. 
	 * A consensus calculation will be performed if the incoming block has a local competitor.
	 * @param block the incoming block to add
	 */
	public void addIncomingBlock(Block block) {
		if( ! catchUpCompleted ) return;

		boolean incomingBlockAdded;
		RejectReason rejectReason = null;
		try {
			rejectReason = consensusBuilder.processExternalBlock(block);
			incomingBlockAdded = RejectReason.NONE.equals(rejectReason);
		} catch (BlockIntegrityException e) {
			incomingBlockAdded = false;
			e.printStackTrace();
		}
		if(incomingBlockAdded) {
			log.info("<<<<<<<<<< Incoming block linked : "+ block.getIndex() + " ("+block.getHash()+")");
			log.debug(block);
			log.debug("Chain : "+chain);
		}
		else {
			log.info("An incoming block has been rejected : "+block);

			if(rejectReason != null 
					&& (rejectReason == RejectReason.COMPETITION 
					|| rejectReason == RejectReason.PREVIOUS_HASH 
					|| rejectReason == RejectReason.PREVIOUS_INDEX)
					)
				// check if blockchain is not in an inconsistent state
				consensusBuilder.checkConsistency();
		}
	}

	/**
	 * Add a block to the blockchain.
	 * Link the new block to the existing chain and send this block to the network.
	 * @param block the block to add
	 */
	public void addBlock(Block block) {

		// link block to current chain
		getChain().linkBlock(block);

		log.info("Chain height : "+getChain().getLastIndex());
		log.debug("Chain : "+chain);

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
	 * Remove from chain all blocks starting by startIndex (included). Data in blocks will be put back in data pool.
	 * @param startIndex
	 */
	public void unlink(long startIndex) {
		log.debug("Unlink from "+startIndex);
		List<ISingleData> dataList = chain.unlinkBlock(startIndex);
		getDataPool().addAll(dataList);
	}

	/**
	 * Starts the Blockchain (network listening and mining)
	 */
	public void start() {
		log.info("Starting blockchain");
		network.start();
		catchUp(this.getLastIndex() + 1);
		if(mining)
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

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append('\n');
		buffer.append("Node : ").append(this.nodeId).append('\n');
		buffer.append("Mining : ").append(this.mining).append('\n');

		return buffer.toString();
	}

	/**
	 * Catch up with existing chain
	 * @param startIndex the index of first missing block
	 */
	public void catchUp(long startIndex) {
		catchUpCompleted = false;
		miner.pauseMining();
		/*
		 * Wait for few peers connection
		 * ask for how many blocks since this.chainHeight
		 */

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		catchUpBuilder = new ChainCatchUpBuilder(this);

		if(catchUpBuilder.startCatchUp(startIndex)) {
			consensusBuilder.setLastLinkedIndex(chain.getLastIndex());
		}
		else {
			log.error("Catch-up failed.");
		}

		catchUpCompleted = true;
		miner.resumeMining();
	}

	/**
	 * Add catch-up incomings blocks to catch-up builder
	 * @param blocks
	 */
	public void addCatchUp(List<Block> blocks) {
		catchUpBuilder.addPendingBlocks(blocks);
	}

	/**
	 * Notify catch-up builder that current chain is empty
	 */
	public void emptyCatchUp() {
		catchUpBuilder.emptyCatchUp();
	}

	/**
	 * Returns the list of peers connected to current node.
	 * @return the list of peers connected to current node.
	 */
	public List<Peer> getPeers() {
		return network.getAlivePeers();
	}

	/**
	 * Send catch-up data to a peer
	 * @param peer the peer to send catch-up data to
	 * @param startIndex the index of first block to catch up
	 */
	public void sendCatchUp(Peer peer, long startIndex) {
		ChainSender sender = new ChainSender(this);
		sender.sendChainToPeer(peer, startIndex);
	}

	/**
	 * Send a message to a peer.
	 * @param messageType the type of message.
	 * @param message the message object.
	 * @param peer the peer to send message to.
	 */
	public void sendMessage(int messageType, NetworkMessage message, Peer peer) {
		network.sendMessage(messageType, message, peer);
	}
}
