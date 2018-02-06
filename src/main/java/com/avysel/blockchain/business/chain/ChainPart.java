package com.avysel.blockchain.business.chain;

import java.util.LinkedList;
import java.util.List;

import com.avysel.blockchain.business.BlockchainManager;
import com.avysel.blockchain.business.block.Block;
import com.avysel.blockchain.exception.ChainIntegrityException;
import com.avysel.blockchain.tools.JsonMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A part of blockchain.
 * It contains the list of Block, but contains no Genesis. The ChainPart is to be appended to an existing Chain
 */
public class ChainPart implements Cloneable {

	private LinkedList<Block> blockList;

	public ChainPart() {
		this.blockList = new LinkedList<Block>();
	}

	public LinkedList<Block> getBlockList() {
		return (LinkedList<Block>) blockList;
	}

	public void setBlockList(LinkedList<Block> blockList) {
		this.blockList = blockList;
	}

	@JsonIgnore
	public Block getFirstBlock() {
		return getBlockList().getFirst();
	}
	@JsonIgnore
	public Block getLastBlock() {
		return getBlockList().getLast();
	}	

	public final void addBlock(Block block) {
		this.getBlockList().add(block);
	}	

	public final void addBlocks(List<Block> blockList) {
		this.getBlockList().addAll(blockList);
	}

	/**
	 * Returns the index of last Block added to the ChainPart.
	 * @return the index of last Block if exists, -1 otherwise.
	 */
	@JsonIgnore
	public long getLastIndex() {
		if(this.getLastBlock() != null) {
			return this.getLastBlock().getIndex();
		} else {
			return -1;
		}
	}	

	/**
	 * Add a new Block to the Chain, and set Block link data (previous hash ...). It does not manage the Genesis Block.
	 * @param block the Block to add
	 */
	public void linkBlock(Block block) {

		if(!block.isGenesis()) {
			block.setPreviousHash(getLastBlock().getHash());
			block.setIndex(getLastIndex() + 1);
			blockList.add(block);
		}
	}

	/**
	 * Returns difficulty of ChainPart (sum of difficulties of all Blocks in the ChainPart).
	 * @return the ChainPart's difficulty
	 */
	@JsonIgnore
	public long getDifficulty() {
		return getBlockList().stream().mapToLong(block -> block.getDifficulty()).sum();
	}

	/**
	 * Returns the effort needed to produce the ChainPart.
	 * The effort is difficulty / number of Blocks.
	 * @return the effort
	 */
	@JsonIgnore
	public long getEffort() {
		return (long) (getDifficulty() / getBlockList().size());
	}

	/**
	 * Add a chain to the end of the current chain.
	 * @param chainPart the chain to add to current chain
	 * @throws ChainIntegrityException occurs when chain integrity is not verified
	 */
	public void addChainPart(ChainPart chainPart) throws ChainIntegrityException {
		synchronized(getBlockList()) {
			// chain part must have a good integrity
			if(!BlockchainManager.checkChain(chainPart)) {
				throw new ChainIntegrityException("ChainPart is corrupted");
			}

			// two parts of chain must be linkable (index must follow each other)
			if(chainPart.getFirstBlock().getIndex() != this.getLastBlock().getIndex() +1) {
				throw new ChainIntegrityException("ChainPart cannot be linked, wrong index");
			}

			// do the link between the two chains
			chainPart.getFirstBlock().setPreviousHash(this.getLastBlock().getHash());

			// add content of new chain to this chain
			this.addBlocks(chainPart.getBlockList());

			// check the result
			if(!BlockchainManager.checkChain(this)) {
				throw new ChainIntegrityException("Result is corrupted");
			}
		}
	}

	/**
	 * Returns ChainPart size (number of blocks).
	 * @return chain part size.
	 */
	public long size() {
		synchronized(getBlockList()) {
			if(this.getBlockList() != null) {
				return getBlockList().size();
			} else {
				return 0;
			}
		}
	}

	/**
	 * Returns the quality of the chain part.
	 * The quality is used when two ChainParts are candidates to be appended to the BlockChain. The one with higher effort is the one that will be appended.
	 * @return chain(s quality
	 */
	@JsonIgnore
	public long getQuality() {
		return getEffort();
	}

	public String toString() {
		String s = "";
		for(Block block : getBlockList()) {
			s+= block.getHash();
			if(block.getIndex() < this.size()-1)
				s+= " -> ";
		}
		return s;
	}

	public void debugDisplay() {
		System.out.println(toString());
	}

	/**
	 * Provides a deep clone of this ChainPart instance. 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ChainPart clone() {
		try {
			super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		String serializedChainPart = JsonMapper.chainToJson(this) ;
		ChainPart deserializedChainPart = JsonMapper.jsonToChain(serializedChainPart);
		return deserializedChainPart;
	}
}
