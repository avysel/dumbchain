package com.avysel.blockchain.business.chainbuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.avysel.blockchain.model.block.Block;

public class ChainBuilder {
	private  Map<String, Block> hashIndexedBlocks;
	private  Map<Long, Block> indexIndexedBlocks;
	private List<Block> timeSortedBlocks;
	
	public ChainBuilder() {
		super();
		hashIndexedBlocks = new HashMap<String,Block>();
		indexIndexedBlocks = new HashMap<Long,Block>();
		timeSortedBlocks = new LinkedList<Block>();
	}
	
	public Map<String, Block> getHashIndexedBlocks() {
		return hashIndexedBlocks;
	}
	public void setHashIndexedBlocks(Map<String, Block> hashIndexedBlocks) {
		this.hashIndexedBlocks = hashIndexedBlocks;
	}
	public Map<Long, Block> getIndexIndexedBlocks() {
		return indexIndexedBlocks;
	}
	public void setIndexIndexedBlocks(Map<Long, Block> indexIndexedBlocks) {
		this.indexIndexedBlocks = indexIndexedBlocks;
	}
	
	public void addPendingBlock(Block block) {
		hashIndexedBlocks.put(block.getHash(), block);
		indexIndexedBlocks.put(block.getIndex(), block);
		timeSortedBlocks.add(block);
		
		Collections.sort(timeSortedBlocks,new BlockTimeComparator());
	}
	
	private class BlockTimeComparator implements Comparator<Block> {
		@Override
		public int compare(Block o1, Block o2) {
			return (int) o1.getTimestamp() - (int) o2.getTimestamp();
		}
	}
}
