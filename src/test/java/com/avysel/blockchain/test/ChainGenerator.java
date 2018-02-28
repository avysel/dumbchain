package com.avysel.blockchain.test;

import com.avysel.blockchain.business.block.Block;
import com.avysel.blockchain.business.chain.ChainPart;
import com.avysel.blockchain.business.data.custom.SingleData;

public final class ChainGenerator {
	
	private ChainGenerator() {}
	
	public static ChainPart createChain(int nbBlocks) {
		ChainPart chain = new ChainPart();
		
		for (int i=1; i<=nbBlocks; i++) {
			Block b = new Block();
			b.setHash("hash"+i);
			b.setIndex(i);
			b.setPreviousHash("hash"+(i-1));
			b.addData(new SingleData("data"+i));
			b.setTimestamp(System.currentTimeMillis()+i);
			b.setMerkleRoot("merkle"+i);
			chain.getBlockList().add(b);
		}
		
		return chain;
	}
}
