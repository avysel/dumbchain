package com.avysel.blockchain;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.data.SingleData;

public class Main {
	public static void main (String[] args) {

	/*	ChainManager manager = new ChainManager();
		
		manager.createChain();
		
		Block block1 = manager.createBlock(Arrays.asList("data1"));
		Block block2 = manager.createBlock(Arrays.asList("data2"));
		Block block3 = manager.createBlock(Arrays.asList("data3"));
		Block block4 = manager.createBlock(Arrays.asList("data4"));
		
		manager.getChain().linkBlock(block1);
		manager.getChain().linkBlock(block2);
		manager.getChain().linkBlock(block3);
		manager.getChain().linkBlock(block4);
		
		manager.display();*/
		
		
		Blockchain manager = new Blockchain();

		manager.createChain();
		
		for(int i = 1 ; i < 100 ; i++)
			manager.addIncomingData(new SingleData("data"+i));
	
		manager.start();
		manager.display();		
		
	}
}
