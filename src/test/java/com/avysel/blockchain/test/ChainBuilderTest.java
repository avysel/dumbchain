package com.avysel.blockchain.test;

import static org.junit.Assert.assertNotNull;

import java.util.Collections;

import org.junit.Test;

import com.avysel.blockchain.business.block.Block;
import com.avysel.blockchain.business.chain.ChainPart;
import com.avysel.blockchain.business.chainbuilder.ChainBuilder;

public class ChainBuilderTest {

	@Test
	public void testChainBuilder() {
		ChainBuilder builder = new ChainBuilder();
		ChainPart chain = ChainGenerator.createChain(1000);
		
		Collections.shuffle(chain.getBlockList());
		
		for(Block b : chain.getBlockList()) {
			builder.addPendingBlock(b);
		}
		
		ChainPart resultChain = builder.build();
		assertNotNull(resultChain);
	}

	@Test public void testChainBuilderByIndex() {
		
		/*
		 * build test forked chain
		 * 
		 * b0 - b1 - b2 - b3 - b4
		 * 		       |_ b5 - b6 - b7 
		 * 		  |_ b8 - b9 - b10 - b11 - b12		
		 * 
		 * 
		 * Expected result :
		 * b0 - b1 - b8 - b9 - b10 - b11 - b12
		 */

		Block b0 = new Block();
		b0.setIndex(0);
		b0.setHash("hb0");
		b0.setPreviousHash(null);		
		
		Block b1 = new Block();
		b1.setIndex(1);
		b1.setHash("hb1");
		b1.setPreviousHash("hb0");
		
		Block b2 = new Block();
		b2.setIndex(2);
		b2.setHash("hb2");
		b2.setPreviousHash("hb1");
		
		Block b3 = new Block();
		b3.setIndex(3);
		b3.setHash("hb3");
		b3.setPreviousHash("hb2");
		
		Block b4 = new Block();
		b4.setIndex(4);
		b4.setHash("hb4");
		b4.setPreviousHash("hb3");
		
		Block b5 = new Block();
		b5.setIndex(3);
		b5.setHash("hb5");
		b5.setPreviousHash("hb2");
		
		Block b6 = new Block();	
		b6.setIndex(4);
		b6.setHash("hb6");
		b6.setPreviousHash("hb5");
		
		Block b7 = new Block();	
		b7.setIndex(5);
		b7.setHash("hb7");
		b7.setPreviousHash("hb6");
		
		Block b8 = new Block();	
		b8.setIndex(2);
		b8.setHash("hb8");
		b8.setPreviousHash("hb1");
		
		Block b9 = new Block();	
		b9.setIndex(3);
		b9.setHash("hb9");
		b9.setPreviousHash("hb8");
		
		Block b10 = new Block();	
		b10.setIndex(4);
		b10.setHash("hb10");
		b10.setPreviousHash("hb9");
		
		Block b11 = new Block();	
		b11.setIndex(5);
		b11.setHash("hb11");
		b11.setPreviousHash("hb10");
		
		Block b12 = new Block();	
		b12.setIndex(6);
		b12.setHash("hb12");
		b12.setPreviousHash("hb11");
		
		ChainBuilder builder = new ChainBuilder();
		builder.addPendingBlock(b1);
		builder.addPendingBlock(b2);
		builder.addPendingBlock(b3);
		builder.addPendingBlock(b4);
		builder.addPendingBlock(b5);
		builder.addPendingBlock(b6);
		builder.addPendingBlock(b7);
		builder.addPendingBlock(b8);
		builder.addPendingBlock(b9);
		builder.addPendingBlock(b10);
		builder.addPendingBlock(b11);
		builder.addPendingBlock(b12);
		
		ChainPart chain = new ChainPart();
		chain.addBlock(b0);
		
		ChainPart result = builder.buildLongestChain(chain);
		System.out.println("=== Result ===");
		result.debugDisplay();
	}
	
}
