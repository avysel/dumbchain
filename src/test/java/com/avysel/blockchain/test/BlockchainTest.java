package com.avysel.blockchain.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.avysel.blockchain.business.ChainManager;
import com.avysel.blockchain.model.SingleData;

public class BlockchainTest {

	@Test
	public void testBlockchain() {
		
		ChainManager manager = new ChainManager();

		manager.createChain();
		
		for(int i = 1 ; i<100;i++)
			manager.addIncomingData(new SingleData("data"+i));
	
		manager.run();
		manager.display();
		
		assertEquals(true, manager.checkChain());
		
	}
	
}
