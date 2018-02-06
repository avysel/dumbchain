package com.avysel.blockchain.business.chainbuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.block.Block;
import com.avysel.blockchain.business.chain.ChainPart;

/**
 * Try to build a chain from a set of blocks.
 */
public class ChainBuilder {

	private static Logger log = Logger.getLogger(ChainBuilder.class);

	// blocks map, index = block hash
	private HashMap<String, Block> hashIndexedBlocks;

	// blocks map, sorted by index value
	private TreeMap<Long, List<Block>> indexIndexedBlocks;

	// blocks map, sorted by increasing creation timestamp
	private TreeMap<Long, Block> timeIndexedBlocks;

	public ChainBuilder() {
		super();
		hashIndexedBlocks = new HashMap<String, Block>();
		indexIndexedBlocks = new TreeMap<Long, List<Block>>(new LongComparator());
		timeIndexedBlocks = new TreeMap<Long, Block>(new LongComparator());
	}

	private static class LongComparator implements Comparator<Long>, Serializable {

		private static final long serialVersionUID = 6814680580996967443L;

		@Override
		public int compare(Long o1, Long o2) {
			return o1.compareTo(o2);
		}
	}	

	/**
	 * Add a block to the builder.
	 * @param block the block to add.
	 */
	public void addPendingBlock(Block block) {
		log.debug("Add pending block : "+block);

		synchronized(hashIndexedBlocks) {
			hashIndexedBlocks.put(block.getHash(), block);
		}

		synchronized(indexIndexedBlocks) {
			List<Block> indexedBlocks = indexIndexedBlocks.get(block.getIndex());
			if(indexedBlocks == null)
				indexedBlocks = new ArrayList<Block>();
			indexedBlocks.add(block);
			indexIndexedBlocks.put(block.getIndex(), indexedBlocks);
		}

		synchronized(timeIndexedBlocks) {
			timeIndexedBlocks.put(block.getTimestamp(), block);
		}
	}

	/**
	 * Try to build a chain according to hashes/previous hashes, and timestamp.
	 * @return a chain composed of blocks stored in the builder, if tries based on hashes and timestamps give consistent result. Null otherwise.
	 */
	public ChainPart build() {
		ChainPart byTime = buildByTimestamp();
		ChainPart byHash = buildByHash();

		boolean timeHash = checkChainEquality(byTime, byHash);

		if(timeHash) {
			return byHash;
		}

		return null;
	}

	/**
	 * Try to build a chain by ordering blocks according to their creation timestamp, based on all blocks stored in the builder.
	 * @return a timestamp ordered chain
	 */
	private ChainPart buildByTimestamp() {
		synchronized(timeIndexedBlocks) {
			ChainPart chain = new ChainPart();
			if(timeIndexedBlocks != null && !timeIndexedBlocks.isEmpty()) {
				for (Map.Entry<Long, Block> entry : timeIndexedBlocks.entrySet()) {
					chain.getBlockList().add(entry.getValue());
				}
			}
			return chain;
		}
	}

	/**
	 * Try to build a chain by linking blocks according to their hashes/previous hashes, based on all blocks stored in the builder.
	 * @return a chain composed of blocks linked by their hash.
	 */
	private ChainPart buildByHash() {
		synchronized(hashIndexedBlocks) {
			ChainPart chain = new ChainPart();
			if(hashIndexedBlocks == null || hashIndexedBlocks.isEmpty()) {
				return chain;
			}
			// get the block with no parent
			Block firstBlock = hashIndexedBlocks.entrySet().stream()
					.filter(map -> getBlockByHash(map.getValue().getPreviousHash()) == null)
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
	}

	/**
	 * Get a block for a given hash, among all blocks stored in the builder.
	 * @param hash the hash.
	 * @return the block with given hash.
	 */
	private Block getBlockByHash(String hash) {
		return hashIndexedBlocks.get(hash);
	}

	/**
	 * Check if two chains are equals (composed of same blocks, in same order).
	 * @param chain1 the first chain to compare.
	 * @param chain2 the second chain to compare.
	 * @return true if the two chains are equals, false otherwise.
	 */
	private boolean checkChainEquality(ChainPart chain1, ChainPart chain2) {

		if (!(chain1 != null && chain2 != null)
				|| chain1.size() != chain2.size())
			return false;

		for (int i = 0; i<chain1.size(); i++) {
			if(!chain1.getBlockList().get(i).equals(chain2.getBlockList().get(i)))
				return false;
		}
		return true;
	}

	/**
	 * Try to build the longest chain, starting from a start chain, completed with blocks stored in ChainBuilder.
	 * @param currentChain the start chain
	 * @return a chain composed of the start chain completed with the longest chain possible built with blocks stored in builder.
	 */
	public ChainPart buildLongestChain(ChainPart currentChain) {
		synchronized(indexIndexedBlocks) {
			if(currentChain == null)
				currentChain = new ChainPart();

			Block lastBlock = currentChain.getLastBlock();
			long index;
			if(lastBlock != null)
				index = lastBlock.getIndex();
			else 
				index = 0;

			List<Block> nextBlocks = indexIndexedBlocks.get(index + 1);
			List<ChainPart> possibleChains = new ArrayList<ChainPart>();
			if(nextBlocks != null) {
				// for every block with next index ...
				for (Block nextBlock : nextBlocks) {
					if(nextBlock.getIndex() == lastBlock.getIndex() + 1
							&& nextBlock.getPreviousHash().equals(lastBlock.getHash())) {
						// ... try to build a new chain by appending this block to the current chain
						ChainPart possibleChain = currentChain.clone();				
						possibleChain.addBlock(nextBlock);
						possibleChain.debugDisplay();
						// then continue to explore next levels
						possibleChains.add(buildLongestChain(possibleChain));
					}
				}
			}

			if (possibleChains.size() == 0)
				possibleChains.add(currentChain);
			return possibleChains.stream().max(Comparator.comparing(ChainPart::size)).get();
		}
	}
}
