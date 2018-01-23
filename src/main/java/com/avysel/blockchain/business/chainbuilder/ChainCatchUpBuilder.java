package com.avysel.blockchain.business.chainbuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.exception.ChainIntegrityException;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.chain.Chain;
import com.avysel.blockchain.model.chain.ChainPart;
import com.avysel.blockchain.network.peer.Peer;

/**
 * Catch up with existing blockchain
 */
public class ChainCatchUpBuilder {
	private static Logger log = Logger.getLogger("com.avysel.blockchain.business.chainbuilder.ChainCatchUpBuilder");

	private static final int CATCH_UP_RETRY_DELAY = 1000;
	private static final int CATCH_UP_MAX_DURATION= 10000;
	
	private Blockchain blockchain;
	private Chain chain;

	private long chainSize;
	private Map<Long, List<Block>> pendingBlocks;

	private ChainRequestor requestor;

	private boolean completed;
	private boolean empty;

	public ChainCatchUpBuilder(Blockchain blockchain) {
		if(blockchain != null) 
			this.chain = blockchain.getChain();
		this.blockchain = blockchain;

		this.pendingBlocks = new HashMap<Long, List<Block>>();
		
		this.requestor = new ChainRequestor(this.blockchain);
		this.completed = false;
		this.empty = false;
	}

	public long getChainSize() {
		return chainSize;
	}

	public void setChainSize(long chainSize) {
		this.chainSize = chainSize;
	}

	public void startCatchUp() {
		log.info("Start to catch up with chain");
		
		long startTime = System.currentTimeMillis();
		
		requestor.requestBlocks();
		while( (!completed && ! empty) && (System.currentTimeMillis() - startTime) <= CATCH_UP_MAX_DURATION) {
			try {
				Thread.sleep(CATCH_UP_RETRY_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			completed = tryBuildingChain();
		}
		log.info("Catch-up completed.");
	}

	public void emptyCatchUp(Peer peer) {
		log.debug("No chain to catch up.");
		empty = true;
	}
	
	public void addPendingBlocks(List<Block> blocks) {
		if(blocks == null) return;
		for(Block block : blocks) {
			addPendingBlock(block);
		}
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

		log.debug("Try to build the chain.");

		if(pendingBlocks != null && ! pendingBlocks.isEmpty()) {

			Long[] indexes = (Long[]) pendingBlocks.keySet().toArray(new Long[pendingBlocks.keySet().size()]);
			Arrays.sort(indexes);

			for (int i = 0 ; i < indexes.length-1 ; i++) {
				if(indexes[i] != indexes[i+1] - 1) {
					log.info("Cannot build chain, missing block.");
					return false;
				}
			}

			log.info("All blocks are arrived, start building chain with "+pendingBlocks.size()+" blocks");

			List<Block> blocks = new LinkedList<Block>();
			for (int i = 0 ; i < indexes.length ; i++) {
				blocks.add(pendingBlocks.get(indexes[i]).get(0));
			}		

			ChainPart chainPart = new ChainPart();
			chainPart.addBlocks(blocks);
			try {
				chain.addChainPart(chainPart);
				blockchain.setInitialChain(chain);
				log.info("Build completed !");
				return true;
			} catch (ChainIntegrityException e) {
				e.printStackTrace();
				log.warn("Error while building chain.");
				return false;
			}
		}
		else {
			log.warn("No block received for catching up, chain can't be initialized");
			return false;
		}
	}
}
