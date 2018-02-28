package com.avysel.blockchain.business;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.block.Block;
import com.avysel.blockchain.business.block.Genesis;
import com.avysel.blockchain.business.chain.Chain;
import com.avysel.blockchain.business.chain.ChainPart;
import com.avysel.blockchain.business.chainbuilder.ChainCatchUpBuilder;
import com.avysel.blockchain.business.chainbuilder.ChainConsensusBuilder;
import com.avysel.blockchain.business.chainbuilder.ChainConsensusBuilder.RejectReason;
import com.avysel.blockchain.business.chainbuilder.ChainSender;
import com.avysel.blockchain.business.data.DataSender;
import com.avysel.blockchain.business.data.ISingleData;
import com.avysel.blockchain.db.DBManager;
import com.avysel.blockchain.demo.RandomDataGenerator;
import com.avysel.blockchain.exception.BlockIntegrityException;
import com.avysel.blockchain.exception.ChainIntegrityException;
import com.avysel.blockchain.mining.DataPool;
import com.avysel.blockchain.mining.Miner;
import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.data.message.NetworkMessage;
import com.avysel.blockchain.network.peer.Peer;

/**
 * The main class of the Blockchain.
 * Contains a unique Chain of Blocks and provides available operations on it.
 */
public class Blockchain {

	private static Logger log = Logger.getLogger(Blockchain.class);

	private BlockchainParameters params;

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

	private DBManager dbManager;

	// build chain according to consensus rules
	private ChainConsensusBuilder consensusBuilder;

	// to catch-up with existing chain
	private ChainCatchUpBuilder catchUpBuilder;

	// to send data to the newtorck if they stay too much time in pool
	private DataSender dataSender;
	
	// is catch-up completed (true) or still in progress (false)
	private boolean catchUpCompleted; // TODO move to catchup builder ?

	public Blockchain() {
		// init with default parameters
		this.params = new BlockchainParameters();
		init();
	}
	
	public Blockchain(BlockchainParameters params) {
		this.params = params;
		init();
	}

	public BlockchainParameters getParams() {
		return params;
	}

	public void setParams(BlockchainParameters params) {
		this.params = params;
	}

	private void init() {
		this.dbManager = new DBManager(getParams().getDbPath());
		initNodeId(); 
		this.chain = new Chain();
		createChain();
		this.dataPool = new DataPool();
		this.miner = new Miner(this, dataPool);
		this.network = new NetworkManager(this);
		this.consensusBuilder = new ChainConsensusBuilder(this);
		this.catchUpCompleted = false;
		this.dataSender = new DataSender(dataPool, network);
	}

	private void initNodeId() {
		String nodeId = dbManager.getStoredNodeId();
		
		if(nodeId != null) {
			this.nodeId = nodeId;
		} else { 
			this.nodeId = UUID.randomUUID().toString();
			dbManager.storeNodeId(this.nodeId);
		}
	}
	
	/**
	 * Returns the unique ID of the current Blockchain node.
	 * @return unique ID of this node.
	 */
	public String getNodeId() {
		return nodeId;
	}

	public Chain getChain() {
		return chain;
	}

	/**
	 * Create the Chain and set a genesis Block.
	 */
	private void createChain() {
		chain = new Chain();
		createGenesis();
	}

	public void setInitialChain(Chain chain) {
		this.chain = chain;
	}

	/**
	 * Returns if the current node can mine blocks.
	 * @return true if the node mines, false otherwise.
	 */
	public boolean isMiningNode() {
		return params.isMiningNode();
	}

	/**
	 * Start/stop miner.
	 * @param mining true to (re)start miner, false to stop miner.
	 */
	public void setMining(boolean mining) {
		if(this.isMiningNode() != mining) {
			this.getParams().setMiningNode(mining);
			if(mining)
				miner.start();
			else
				miner.stop();
		}
	}	

	/**
	 * Create the genesis Block and add it to the Chain.
	 */
	private void createGenesis() {
		Block genesis = new Genesis();
		chain.linkBlock(genesis);
		genesis.setPreviousHash(null);
	}

	/**
	 * Gets the Block index of last Block linked at the end of the Chain.
	 * @return the last Block index.
	 */
	public long getLastIndex() {
		if(this.getChain().getLastBlock() != null) {
			return this.getChain().getLastBlock().getIndex();
		} else {
			return Genesis.GENESIS_INDEX;
		}
	}

	/**
	 * Display the Chain from last Block to Genesis Block.
	 */
	public void display() {
		Block currentBlock = chain.getLastBlock();

		while(currentBlock != null) {
			System.out.println(currentBlock);			
			currentBlock = BlockchainManager.findBlockByHash(chain, currentBlock.getPreviousHash());
		}
	}

	/**
	 * Save blockchain.
	 */
	public void save() {
		dbManager.putChain(this.getNodeId(), this.getChain());
	}

	/**
	 * Load existing Chain from database.
	 */
	public void load() {
		log.info("Start loading from DB");

		if(this.getNodeId() != null) {
			Chain loadedChain = dbManager.getChain(this.getNodeId());
			if(loadedChain != null) {
				this.chain = loadedChain;
				log.info("Local chain has been loaded, starting from index "+chain.getLastIndex());
			} else {
				log.info("No chain found for local node.");
			}
		} else {
			log.info("No local chain to load.");
		}
	}

	/**
	 * Returns the data pool of current blockchain.
	 * @return the data pool.
	 */
	public DataPool getDataPool() {
		return this.dataPool;
	}

	/**
	 * Add a new data, coming from network, to be included in a block in one of the next mining.
	 * @param data the incoming data.
	 * @throws InterruptedException if synchronize error occurs.
	 */
	public void addIncomingData(ISingleData data) throws InterruptedException {
		dataPool.addData(data);
	}

	/**
	 * Add a new block to be linked at the end of the chain. 
	 * A consensus calculation will be performed if the incoming block has a local competitor.
	 * @param block the incoming block to add.
	 */
	public void addIncomingBlock(Block block) {
		if(!catchUpCompleted) return;

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
			dataSender.setLastBlockTimestamp(block.getTimestamp());
			// save new chain
			save();
			log.debug("Incoming block linked : "+ block.getIndex() + " ("+block.getHash()+")");
			log.debug(block);
			log.debug("Chain : "+chain);
		} else {
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
	 * @param block the block to add.
	 */
	public void addBlock(Block block) {

		// link block to current chain
		getChain().linkBlock(block);

		log.info("Chain height : "+getChain().getLastIndex());
		log.debug("Chain : "+chain);

		// send block to the network
		network.sendBlock(block);
		
		dataSender.setLastBlockTimestamp(block.getTimestamp());
		
		// save new chain
		save();
	}

	/**
	 * Add a new data, created by current node, to the data pool, and send it to the newtork.
	 * @param data the data to add.
	 * @throws InterruptedException if synchronization error occurs.
	 */
	public void addData(ISingleData data) throws InterruptedException {
		log.debug("Add new data : "+data);
		addIncomingData(data);
		network.sendData(data);
	}

	/**
	 *  Add a subchain to the end of the current Blockchain.
	 *  Use this method when a subchain is obtained with mining or by the network, and is to be appended to the Blockchain.
	 *  The index of subchain's first Block must be current's chain last Block +1.
	 * @param subChain the subchain to add.
	 * @throws ChainIntegrityException if integrity of subchain or final chain is not verified.
	 */
	public void addSubChain(ChainPart subChain) throws ChainIntegrityException {
		this.getChain().addChainPart(subChain);
	}

	/**
	 * Remove from chain all blocks starting by startIndex (included). Data in blocks will be put back in data pool.
	 * @param startIndex the index of first block to remove.
	 */
	public void unlink(long startIndex) {
		log.debug("Unlink from "+startIndex);
		List<ISingleData> dataList = chain.unlinkBlock(startIndex);
		getDataPool().addAll(dataList);
	}

	/**
	 * Starts the Blockchain (network listening and mining).
	 */
	public void start() {
		log.info("Starting blockchain");

		load();

		network.start();
		catchUp(this.getLastIndex());

		if(params.isDemoDataGenerator()) {
			(new RandomDataGenerator(this)).start();
		}

		if(isMiningNode()) {
			miner.start();
		}
		
		dataSender.start();
	}

	/**
	 * Stops the Blockchain (network listening and mining).
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
		buffer.append("Mining : ").append(this.isMiningNode()).append('\n');

		return buffer.toString();
	}

	/**
	 * Catch up with existing chain.
	 * @param lastIndex the index of last known block.
	 */
	public void catchUp(long lastIndex) {
		catchUpCompleted = false;
		miner.pauseMining();

		/*
		 * Wait for few peers connection
		 * ask for how many blocks since this.chainHeight
		 */
		try {
			Thread.sleep(getParams().getWaitForPeersTime());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		catchUpBuilder = new ChainCatchUpBuilder(this);

		if(catchUpBuilder.startCatchUp(lastIndex)) {
			consensusBuilder.setLastLinkedIndex(chain.getLastIndex());
		} else {
			log.error("Catch-up failed.");
		}

		catchUpCompleted = true;
		save();

		miner.resumeMining();
	}

	/**
	 * Add catch-up incomings blocks to catch-up builder.
	 * @param blocks the list of blocks to add to the current chain to catch-up.
	 */
	public void addCatchUp(List<Block> blocks) {
		catchUpBuilder.addPendingBlocks(blocks);
	}

	/**
	 * Notify catch-up builder that current chain is empty.
	 */
	public void markAsUpToDate() {
		catchUpBuilder.markAsUpToDate();
	}

	/**
	 * Returns the list of peers connected to current node.
	 * @return the list of peers connected to current node.
	 */
	public List<Peer> getPeers() {
		return network.getAlivePeers();
	}

	/**
	 * Send catch-up data to a peer.
	 * @param peer the peer to send catch-up data to.
	 * @param startIndex the index of first block to catch up.
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
