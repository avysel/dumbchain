package com.avysel.blockchain.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.business.BlockchainManager;
import com.avysel.blockchain.business.block.Block;
import com.avysel.blockchain.business.data.SingleData;
import com.avysel.blockchain.crypto.HashTools;
import com.avysel.blockchain.mining.DataPool;
import com.avysel.blockchain.mining.Miner;

public class ConsensusTest {

	private Blockchain manager = new Blockchain();
	private DataPool pool = new DataPool();
	private Miner miner = new Miner(manager, pool);

	public ConsensusTest() {

	}

	@Test
	public void init() {
		for(int i=1; i<100; i++)
			try {
				manager.addIncomingData(new SingleData("data"+i));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


		for(int i=100; i<200; i++)
			try {
				pool.addData(new SingleData("data"+i));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		manager.display();			

		Block incomingBlock = null;
		long size = manager.getChain().size();
		// add some valid blocks
		for(int i=0; i<5; i++) {
			incomingBlock = miner.mine();
			manager.addIncomingBlock(incomingBlock);

			assertEquals(++size, manager.getChain().size());		
		}

		size = manager.getChain().size();
		// add twice the same block, should be rejected
		manager.addIncomingBlock(incomingBlock);
		assertEquals(size, manager.getChain().size());			

		size = manager.getChain().size();
		// add a block with wrong hash, should be rejected
		Block incomingBlock2 = miner.mine();
		incomingBlock2.setHash("45454");
		manager.addIncomingBlock(incomingBlock2);
		assertEquals(size, manager.getChain().size());		

		size = manager.getChain().size();
		// add a block with wrong proof of work, should be rejected
		Block block3 = new Block();
		block3.addData(new SingleData("data00"));
		block3.setHash(HashTools.calculateBlockHash(block3));
		manager.addIncomingBlock(block3);
		assertEquals(size, manager.getChain().size());		

		manager.display();	
		assertEquals(true, BlockchainManager.checkChain(manager.getChain()));
	}
}
