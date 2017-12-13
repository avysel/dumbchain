package com.avysel.blockchain.model.chain;

import java.util.ArrayList;
import java.util.List;

import com.avysel.blockchain.business.BlockchainManager;
import com.avysel.blockchain.exception.ChainIntegrityException;
import com.avysel.blockchain.model.block.Block;

/**
 * A part of blockchain.
 * It contains the list of @Block, but contains no @Genesis. The @ChainPart is to be appended to an existing @Chain
 */
public class ChainPart {
	protected ArrayList<Block> blockList;
	protected Block firstBlock;
	protected Block lastBlock;
	
	public ArrayList<Block> getBlockList() {
		return blockList;
	}

	public void setBlockList(ArrayList<Block> blockList) {
		this.blockList = blockList;
	}
	
	public Block getFirstBlock() {
		return firstBlock;
	}

	public void setFirstBlock(Block firstBlock) {
		this.firstBlock = firstBlock;
	}

	public Block getLastBlock() {
		return lastBlock;
	}

	public void setLastBlock(Block lastBlock) {
		this.lastBlock = lastBlock;
	}	

	private void addBlocks(List<Block> blockList) {
		this.getBlockList().addAll(blockList);
	}
	
	/**
	 * Returns the index of last @Block added to the @ChainPart
	 * @return the index of last @Block if exists, -1 otherwise.
	 */
	public long getLastIndex() {
		if(this.getLastBlock() != null) {
			return this.getLastBlock().getIndex();
		}
		else {
			return -1;
		}
	}	
	
	/**
	 * Add a new @Block to the @Chain, and set @Block link data (previous hash ...). It does not manage the @Genesis @Block.
	 * @param block the @Block to add
	 */
	public void linkBlock(Block block) {
		
		if(! block.isGenesis()) {
			block.setPreviousHash(this.lastBlock.getHash());
			block.setIndex(getLastIndex() + 1);
			blockList.add(block);
			lastBlock = block;
		}
	}
	
	/**
	 * Remove a block from the chain, for example when a better block was received from the network.
	 * Its following blocks are removed too.
	 * @param block the starting point of sub chain to remove
	 */
	public List<Block> unlinkBlock(Block block) {
		// TODO
		// retourner la liste des blocks supprimés
		return null;
	}
	
	/**
	 * Returns difficulty of @ChainPart (sum of difficulties of all @Blocks in the @ChainPart).
	 * @return @ChainPart difficulty
	 */
	public long getDifficulty() {
		return getBlockList().stream().mapToLong(block -> block.getDifficulty()).sum();
	}
	
	/**
	 * Returns the effort needed to produce de @ChainPart.
	 * The effort is difficulty / number of @Blocks.
	 * The effort is used when two @ChainParts are candidates to be appended to the @BlockChain. The one with higher effort is the one that will be appended.
	 * @return
	 */
	public float getEffort() {
		return getDifficulty() / getBlockList().size();
	}
	
	/**
	 * Add a subchain to the end of the current chain
	 * @param chainPart
	 * @throws ChainIntegrityException
	 */
	public void addChainPart(ChainPart chainPart) throws ChainIntegrityException {
		
		// chain part must have a good integrity
		if( ! BlockchainManager.checkChain(chainPart) ) {
			throw new ChainIntegrityException("ChainPart is corrupted");
		}
		
		// two parts of chain must be linkable (index must follow each other)
		if( chainPart.getFirstBlock().getIndex() != this.getLastBlock().getIndex() +1 ) {
			throw new ChainIntegrityException("ChainPart cannot be linked, wrong index");
		}
		
		// do the link between the two chains
		chainPart.getFirstBlock().setPreviousHash(this.getLastBlock().getHash());
		
		// add content of new chain to this chain
		this.addBlocks(chainPart.getBlockList());
		
		// check the result
		if( ! BlockchainManager.checkChain(this) ) {
			throw new ChainIntegrityException("Result is corrupted");
		}
	}
	
	public long size() {
		if(this.blockList != null) {
			return blockList.size();
		}
		else {
			return 0;
		}
	}
}
