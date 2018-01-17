package com.avysel.blockchain.business.chainbuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.exception.ChainIntegrityException;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.chain.Chain;
import com.avysel.blockchain.model.chain.ChainPart;

/**
 * Try to build current chain for a new node
 */
public class ChainBeginnerBuilder {
	private static Logger log = Logger.getLogger("com.avysel.blockchain.business.chainbuilder.ChainBeginnerBuilder");

	private Blockchain blockchain;
	private Chain chain;
	
	private long chainSize;
	private Map<Long, List<Block>> pendingBlocks;
	
	public ChainBeginnerBuilder(Blockchain blockchain) {
		if(blockchain != null) 
			this.chain = blockchain.getChain();
		this.blockchain = blockchain;
	}
	
	public long getChainSize() {
		return chainSize;
	}

	public void setChainSize(long chainSize) {
		this.chainSize = chainSize;
	}

	public void addPendingBlock(Block block) {
		
		List<Block> sameIndexBlocks = pendingBlocks.get(block.getIndex());
		if(sameIndexBlocks == null) {
			sameIndexBlocks = new ArrayList<Block>();
			pendingBlocks.put(block.getIndex(), sameIndexBlocks);
		}
		sameIndexBlocks.add(block);
	}
	
	public boolean tryBuildingChain() {
		
		log.info("Try to build the chain.");
		
		Long[] indexes = (Long[]) pendingBlocks.keySet().toArray();
		Arrays.sort(indexes);
		
		for (int i = 0 ; i < indexes.length-1 ; i++) {
			if(indexes[i] != indexes[i+1] - 1) {
				log.info("Cannot build chain, missing block.");
				return false;
			}
		}
		
		log.info("All blocks are present, start building chain.");
		
		List<Block> blocks = new LinkedList<Block>();
		for (int i = 0 ; i < indexes.length ; i++) {
			blocks.add(pendingBlocks.get(indexes[i]).get(0));
		}		
		
		ChainPart chainPart = new ChainPart();
		chainPart.addBlocks(blocks);
		try {
			chain.addChainPart(chainPart);
			blockchain.setInitialChain(chain);
			log.info("Building completed !");
			return true;
		} catch (ChainIntegrityException e) {
			e.printStackTrace();
			log.warn("Error while building chain.");
			return false;
		}
		
	}
	
}
