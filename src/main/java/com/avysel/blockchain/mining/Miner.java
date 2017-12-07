package com.avysel.blockchain.mining;

import java.util.ArrayList;
import java.util.List;

import com.avysel.blockchain.crypto.HashTools;
import com.avysel.blockchain.mining.proof.IProof;
import com.avysel.blockchain.mining.proof.ProofOfWork;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.chain.Chain;
import com.avysel.blockchain.model.data.ISingleData;

/**
 * The Miner creates Blocks with pending data. Each try of create a Block use a random quantity of data. If a Block can be created, it's linked to the Chain
 *  and sent to the network. If a Block can't be created with peeked data, creation is canceled, data is put back to pending data, and a new try is started.
 */
public class Miner {

	// mining or not ?
	private boolean mining;

	// the queue of pending data to be included in a Block
	private PendingData pendingData;

	// The current existing Chain
	private Chain chain;

	private IProof proof;

	/**
	 * Creates the Miner
	 * @param chain the Chain that will gets new Blocks
	 * @param pendingData the queue to peek data
	 */
	public Miner(Chain chain, PendingData pendingData) {
		mining = true;
		this.pendingData = pendingData;
		this.chain = chain;
		this.proof = new ProofOfWork(); // TODO inject ?
	}

	/**
	 * Starts mining
	 */
	public void start() {
		System.out.println("Start miner.");
		while(mining) {
			Block block = mine();
			chain.linkBlock(block);
			System.out.println(block);
			System.out.println("New block created with "+block.getDataList().size()+" data. Remaining : "+pendingData.size());			
		}
		System.out.println("End miner.");
		System.out.println("Effort : "+chain.getEffort());		
	}

	/**
	 * Stop mining
	 */
	public void stop() {
		mining = false;
	}


	/**
	 * Create a @Block
	 * @return a @Block that contains random data taken from @PendingData
	 */
	private Block mine() {
		Block block;
		List<ISingleData> dataList = new ArrayList<ISingleData>();

		// TODO quel element a changer pour avoir un nouveau hash a chaque tentative ?
		// si on utilise le timestamp, le check ne fonctionnera pas
		String hash;
		long difficulty = 0;
		do {
			
			block = new Block();	
			// put unused data back to pending data
			pendingData.addAll(dataList);

			// clean current data set
			block.cleanData();

			// pick new dataset, blocking when pending data is empty
			dataList = pendingData.getRandomData();
			block.addAllData(dataList);

			hash = HashTools.calculateBlockHash(block);

			block.setHash(hash);
			block.setTimestamp(System.currentTimeMillis());
			block.setDifficulty(difficulty);

			difficulty ++;
			
		} while (! proof.checkCondition(block) ); // try again if pow is not checked

		return block;
	}

}
