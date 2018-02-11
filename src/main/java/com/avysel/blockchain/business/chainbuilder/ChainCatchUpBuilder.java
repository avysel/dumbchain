package com.avysel.blockchain.business.chainbuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.business.block.Block;
import com.avysel.blockchain.business.chain.Chain;
import com.avysel.blockchain.business.chain.ChainPart;
import com.avysel.blockchain.exception.ChainIntegrityException;

/**
 * Catch up with existing blockchain.
 */
public class ChainCatchUpBuilder {
	private static Logger log = Logger.getLogger(ChainCatchUpBuilder.class);

	private static final int CATCH_UP_RETRY_DELAY = 1000;
	
	private Blockchain blockchain;
	private Chain chain;

	private long chainSize;
	private Map<Long, List<Block>> pendingBlocks;

	private ChainRequestor requestor;

	private CatchUpResult completed;
	private boolean getEmptyDataResult;
	
	private enum CatchUpResult {
		NO_TRY,
		CATCH_UP_FAILED,
		CATCH_UP_SUCCESSFUL,
		CATCH_UP_EMPTY
	}

	public ChainCatchUpBuilder(Blockchain blockchain) {
		if(blockchain != null) 
			this.chain = blockchain.getChain();
		this.blockchain = blockchain;
		this.pendingBlocks = new HashMap<Long, List<Block>>();
		this.requestor = new ChainRequestor(this.blockchain);
		this.completed = CatchUpResult.NO_TRY;
		this.getEmptyDataResult = false;
	}

	public long getChainSize() {
		return chainSize;
	}

	public void setChainSize(long chainSize) {
		this.chainSize = chainSize;
	}

	public boolean startCatchUp(long lastIndex) {
		log.info("Start to catch up with chain");
		
		long startTime = System.currentTimeMillis();
		
		// send catch cup request
		requestor.requestBlocks(lastIndex);
		
		// wait to get catch up data or build successfull
		while(completed != CatchUpResult.CATCH_UP_SUCCESSFUL 
				&& !getEmptyDataResult 
				&& (System.currentTimeMillis() - startTime) <= blockchain.getParams().getProperties().getWaitForCatchupTime()) {
			
			// wait a few time bewteen two tries
			try {
				Thread.sleep(CATCH_UP_RETRY_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// try build chain with existing blocks
			completed = tryBuildingChain();
		}
		log.info("Catch-up completed : "+completed);
		
		// if successful or nothing to catch up -> resume blockchain starting
		return completed == CatchUpResult.CATCH_UP_SUCCESSFUL || completed == CatchUpResult.CATCH_UP_EMPTY;
	}
	
	public void emptyCatchUp() {
		log.debug("No chain to catch up.");
		getEmptyDataResult = true;
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

	public CatchUpResult tryBuildingChain() {

		log.debug("Try to build the chain.");

		if(pendingBlocks != null && !pendingBlocks.isEmpty()) {

			// check if all indexes are present
			Long[] indexes = (Long[]) pendingBlocks.keySet().toArray(new Long[pendingBlocks.keySet().size()]);
			Arrays.sort(indexes);

			for (int i = 0; i<indexes.length-1; i++) {
				if(indexes[i] != indexes[i+1] - 1) {
					log.info("Cannot build chain, missing block."+indexes[i]+" -> "+indexes[i+1]);
					return CatchUpResult.CATCH_UP_FAILED;
				}
			}

			log.info("All blocks are arrived, start building chain with "+pendingBlocks.size()+" blocks");

			// link blocks for each index
			List<Block> blocks = new LinkedList<Block>();
			for (int i=0; i<indexes.length; i++) {
				blocks.add(pendingBlocks.get(indexes[i]).get(0));
			}		

			ChainPart chainPart = new ChainPart();
			chainPart.addBlocks(blocks);
			try {
				// init chain with catch-up data
				chain.addChainPart(chainPart);
				blockchain.setInitialChain(chain);
				log.info("Build completed !");
				return CatchUpResult.CATCH_UP_SUCCESSFUL;
			} catch (ChainIntegrityException e) {
				// catch-up fail on data integrity
				e.printStackTrace();
				log.warn("Error while building chain.");
				return CatchUpResult.CATCH_UP_FAILED;
			}
		} else {
			log.warn("No block received for catching up, chain can't be initialized");
			return CatchUpResult.CATCH_UP_EMPTY;
		}
	}
}
