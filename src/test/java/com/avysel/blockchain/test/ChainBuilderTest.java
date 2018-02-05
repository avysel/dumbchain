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

}
