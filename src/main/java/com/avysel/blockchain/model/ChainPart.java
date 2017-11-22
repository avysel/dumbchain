package com.avysel.blockchain.model;

import java.util.ArrayList;
import java.util.List;

import com.avysel.blockchain.business.BlockchainManager;
import com.avysel.blockchain.exception.ChainIntegrityException;

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
	
	public void addChainPart(ChainPart chainPart) throws ChainIntegrityException {
		
		if( ! BlockchainManager.checkChain(chainPart) ) {
			throw new ChainIntegrityException("ChainPart is corrupted");
		}
		
		if( chainPart.getFirstBlock().getIndex() != this.getLastBlock().getIndex() +1 ) {
			throw new ChainIntegrityException("ChainPart cannot be linked, wrong index");
		}
		
		// do the link between the two chains
		chainPart.getFirstBlock().setPreviousHash(this.getLastBlock().getHash());
		
		// add content of new chain to this chain
		this.addBlocks(chainPart.getBlockList());
		
		if( ! BlockchainManager.checkChain(this) ) {
			throw new ChainIntegrityException("Result is corrupted");
		}
	}
}
