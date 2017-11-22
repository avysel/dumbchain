package com.avysel.blockchain.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.avysel.blockchain.business.BlockchainManager;
import com.avysel.blockchain.model.SingleData;

public class BlockchainTest {

	@Test
	public void testBlockchain() {
		
		BlockchainManager manager = new BlockchainManager();

		manager.createChain();
		
		for(int i = 1 ; i<100;i++)
			manager.addIncomingData(new SingleData("data"+i));
	
		manager.run();
		manager.display();
		
		assertEquals(true, manager.checkChain());
		
	}
	
}
