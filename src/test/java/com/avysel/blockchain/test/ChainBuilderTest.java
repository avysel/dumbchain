package com.avysel.blockchain.test;

import java.util.Collections;

import org.junit.Test;

import com.avysel.blockchain.business.chainbuilder.ChainBuilder;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.chain.ChainPart;

public class ChainBuilderTest {

	@Test
	public void testChainBuilder() {
		ChainBuilder builder = new ChainBuilder();
		ChainPart chain = ChainGenerator.createChain(10);
		
		Collections.shuffle(chain.getBlockList());
		
		for(Block b : chain.getBlockList()) {
			builder.addPendingBlock(b);
		}
		
		builder.build();
	}

}
