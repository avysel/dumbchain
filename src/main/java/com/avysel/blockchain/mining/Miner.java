package com.avysel.blockchain.mining;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.avysel.blockchain.crypto.HashTools;
import com.avysel.blockchain.mining.proof.IProof;
import com.avysel.blockchain.mining.proof.ProofOfWork;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.chain.Chain;
import com.avysel.blockchain.model.data.SingleData;

/**
 * The Miner creates Blocks with pending data. Each try of create a Block use a random quantity of data. If a Block can be created, it's linked to the Chain
 *  and sent to the network. If a Block can't be created with peeked data, creation is canceled, data is put back to pending data, and a new try is started.
 */
public class Miner {

	private static Logger log = Logger.getLogger("com.avysel.blockchain.mining.Miner");
	
	// mining or not ?
	private boolean mining;

	// the queue of pending data to be included in a Block
	private DataPool dataPool;

	// The current existing Chain
	private Chain chain;

	private IProof proof;

	/**
	 * Creates the Miner
	 * @param chain the Chain that will gets new Blocks
	 * @param dataPool the queue to peek data
	 */
	public Miner(Chain chain, DataPool dataPool) {
		mining = true;
		this.dataPool = dataPool;
		this.chain = chain;
		this.proof = new ProofOfWork();
	}

	/**
	 * Starts mining
	 */
	public void start() {
		log.info("Start miner.");
		while(mining) {
			Block block = mine();
			chain.linkBlock(block);
			log.info("New block created with "+block.getDataList().size()+" data. "+dataPool.size() +" data in pool. Chain size : "+chain.size());
			log.debug(block);
		}
		log.info("End miner.");
		log.debug("Effort : "+chain.getEffort());		
	}

	/**
	 * Stop mining
	 */
	public void stop() {
		mining = false;
	}


	/**
	 * Create a @Block
	 * @return a @Block that contains random data taken from @DataPool
	 */
	private Block mine() {
		Block block;
		List<SingleData> dataList = new ArrayList<SingleData>();

		String hash;
		long difficulty = 0;
		do {
			
			block = new Block();	
			// put unused data back to pending data
			dataPool.addAll(dataList);

			// clean current data set
			block.cleanData();

			// pick new dataset, blocking when pending data is empty
			dataList = dataPool.getRandomData();
			block.addAllData(dataList);

			hash = HashTools.calculateBlockHash(block);

			log.debug("Hash : "+hash);
			
			block.setHash(hash);
			block.setTimestamp(System.currentTimeMillis());
			block.setDifficulty(difficulty);

			difficulty ++;
			
		} while (! proof.checkCondition(block) ); // try again if pow is not checked

		return block;
	}

}
