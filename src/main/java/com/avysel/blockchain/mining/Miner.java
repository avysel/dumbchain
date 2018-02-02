package com.avysel.blockchain.mining;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.business.BlockchainParameters;
import com.avysel.blockchain.business.block.Block;
import com.avysel.blockchain.business.data.ISingleData;
import com.avysel.blockchain.crypto.HashTools;
import com.avysel.blockchain.crypto.MerkleTree;
import com.avysel.blockchain.mining.proof.IProof;
import com.avysel.blockchain.mining.proof.ProofOfWork;

/**
 * The Miner creates Blocks with pending data. Each try of create a Block use a random quantity of data. If a Block can be created, it's linked to the Chain
 *  and sent to the network. If a Block can't be created with peeked data, creation is canceled, data is put back to pending data, and a new try is started.
 */
public class Miner {

	private static Logger log = Logger.getLogger(Miner.class);

	// mining node or not ?
	private boolean miningNode;

	// the queue of pending data to be included in a Block
	private DataPool dataPool;

	// the current blockchain
	private Blockchain blockchain;

	// the condition to validate a block
	private IProof proof;

	// mining is pending or running ?
	private boolean pauseMining;



	/**
	 * Creates the Miner
	 * @param blockchain the blockchain that will gets new Blocks
	 * @param dataPool the queue to peek data
	 */
	public Miner(Blockchain blockchain, DataPool dataPool) {
		miningNode = true;
		this.dataPool = dataPool;
		this.blockchain = blockchain;
		this.proof = new ProofOfWork();
	}

	/**
	 * Starts mining
	 */
	public void start() {
		log.info("Start miner.");
		// while mining blockchain node is running
		while(miningNode) {
		
			// if mining is not pending, and enough data in pool
			if(! pauseMining && dataPool.size() > 0) {
				// create new block
				Block block = mine();	

				log.info("New block created with "+block.getDataList().size()+" data. "+dataPool.size() +" data in pool. ("+block.getHash()+")");
				log.debug(block);

				// add block to blockchain
				blockchain.addBlock(block);		
			}
		}
		log.info("End miner.");
		log.debug("Effort : "+blockchain.getChain().getEffort());		
	}

	/**
	 * Stop mining
	 */
	public void stop() {
		miningNode = false;
	}


	/**
	 * Create a Block
	 * @return a Block that contains random data taken from DataPool
	 */
	public Block mine() {
		Block block = new Block();
		
		// pick new dataset, blocking when pending data is empty
		List<ISingleData> dataList = dataPool.pickData(BlockchainParameters.MAX_DATA_IN_BLOCK);

		block.addAllData(dataList);	
		block.setMerkleRoot(MerkleTree.computeMerkleRoot(block));
		
		String hash;
		long difficulty = 0;
		do { 
			if( ! pauseMining ) {

				// all block creation timestamps are based on GMT+0 timezone
				TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
				block.setTimestamp(Timestamp.from(Instant.now()).getTime());
				block.setDifficulty(difficulty);				
				
				hash = HashTools.calculateBlockHash(block);

				log.trace("Hash : "+hash);

				block.setHash(hash);

				difficulty ++;
			}
		} while (pauseMining || ! proof.checkCondition(block) ); // try again if pow is not checked

		return block;
	}

	/**
	 * Suspend mining. If a block is being mined, it will be dissmissed.
	 */
	public void pauseMining() {
		pauseMining = true;
	}

	/**
	 * Resume mining. Starts the mining of a new node.
	 */
	public void resumeMining() {
		pauseMining = false;
	}
}
