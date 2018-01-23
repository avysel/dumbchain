package com.avysel.blockchain.business.chainbuilder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.chain.ChainPart;

public class ChainBuilder {

	// blocks map, index = block hash
	private  HashMap<String, Block> hashIndexedBlocks;

	// blocks map, sorted by index value
	private  TreeMap<Long, Block> indexIndexedBlocks;

	// blocks map, sorted by increasing creation timestamp
	private TreeMap<Long, Block> timeIndexedBlocks;

	public ChainBuilder() {
		super();
		hashIndexedBlocks = new HashMap<String, Block>();
		indexIndexedBlocks = new TreeMap<Long, Block>(new LongComparator());
		timeIndexedBlocks = new TreeMap<Long, Block>(new LongComparator());
	}

	private class LongComparator implements Comparator<Long> {
		@Override
		public int compare(Long o1, Long o2) {
			return o1.compareTo(o2);
		}
	}	

	public void addPendingBlock(Block block) {
		hashIndexedBlocks.put(block.getHash(), block);
		indexIndexedBlocks.put(block.getIndex(), block);
		timeIndexedBlocks.put(block.getTimestamp(), block);
	}

	public void build() {
		ChainPart byIndex = buildByIndex();
		ChainPart byTime = buildByTimestamp();
		ChainPart byHash = buildByHash();
	}

	private ChainPart buildByIndex() {
		return buildByMap(indexIndexedBlocks);
	}

	private ChainPart buildByTimestamp() {
		return buildByMap(timeIndexedBlocks);
	}

	private ChainPart buildByMap(TreeMap<Long, Block> map) {
		ChainPart chain = new ChainPart();
		if(map != null && !map.isEmpty()) {
			for (Map.Entry<Long, Block> entry : map.entrySet()) {
				chain.getBlockList().add(entry.getValue());
			}
		}
		return chain;		
	}

	private ChainPart buildByHash() {
		ChainPart chain = new ChainPart();
		if(hashIndexedBlocks == null || hashIndexedBlocks.isEmpty()) {
			return chain;
		}
		// get the block with no parent
		Block firstBlock = hashIndexedBlocks.entrySet().stream()
				.filter( map -> getBlockByHash(map.getValue().getPreviousHash()) == null )
				.findFirst().get().getValue();

		chain.getBlockList().add(firstBlock);

		Block current = firstBlock;
		boolean found = true;
		while (found) {
			found = false;
			for (Map.Entry<String, Block> entry : hashIndexedBlocks.entrySet()) {
				if(entry.getValue().getPreviousHash().equals(current.getHash())) {
					chain.getBlockList().add(entry.getValue());
					current = entry.getValue();
					found = true;
				}
			}
		}

		return chain;
	}

	private Block getBlockByHash(String hash) {
		return hashIndexedBlocks.get(hash);
	}
	
	private boolean checkChainEquality(ChainPart chain1, ChainPart chain2, ChainPart chain3) {
		return true;
	}
}
