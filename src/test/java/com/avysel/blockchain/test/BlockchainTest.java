package com.avysel.blockchain.test;

import org.junit.Test;

import com.avysel.blockchain.model.Block;
import com.avysel.blockchain.business.ChainManager;

public class BlockchainTest {

	@Test
	public void testBlockchain() {
		
		ChainManager manager = new ChainManager();
		
		manager.createChain();
		
		Block block1 = manager.createBlock("data1");
		Block block2 = manager.createBlock("data2");
		Block block3 = manager.createBlock("data3");
		Block block4 = manager.createBlock("data4");
		
		manager.getChain().linkBlock(block1);
		manager.getChain().linkBlock(block2);
		manager.getChain().linkBlock(block3);
		manager.getChain().linkBlock(block4);
		
		manager.display();
		
	}
	
}
