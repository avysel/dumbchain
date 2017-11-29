package com.avysel.blockchain.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.business.BlockchainManager;
import com.avysel.blockchain.model.data.SingleData;

public class BlockchainTest {

	@Test
	public void testBlockchain() {
		
		Blockchain manager = new Blockchain();

		manager.createChain();
		
		for(int i = 1 ; i < 100 ; i++)
			manager.addIncomingData(new SingleData("data"+i));
	
		manager.startNode();
		manager.display();
		
		assertEquals(true, BlockchainManager.checkChain(manager.getChain()));
		
	}
	
}
