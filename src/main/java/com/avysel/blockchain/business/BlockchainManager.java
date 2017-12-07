package com.avysel.blockchain.business;

import java.util.List;

import com.avysel.blockchain.crypto.HashTools;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.chain.ChainPart;

/**
 * Provides some operation on the Blockchain, such as finding a Block or checking Chain integrity.
 */
public class BlockchainManager {
	
	/**
	 * Find a @Block with a given index
	 * @param index the index to find
	 * @return the @Block with the given index
	 */
	public static Block findBlockByIndex(ChainPart chain, long index) {
	
		for(Block block : chain.getBlockList()){
			if(block.getIndex() == index)
				return block;
		}
		return null;
	}
	
	/**
	 * Find a @Block with a given hash
	 * @param hash the hash
	 * @return the @Block with the given hash
	 */
	public static Block findBlockByHash(ChainPart chain, String hash) {
		
		if(hash == null) return null;
		
		for(Block block : chain.getBlockList()){
			if(block.getHash() != null 
				&& block.getHash().equals(hash))
				return block;
		}
		return null;
	}	
	
	/**
	 * Perform integrity check for the @Chain
	 * @return true if @Chain integrity is good
	 */
	public static boolean checkChain(ChainPart chain) {
		List<Block> blockList = chain.getBlockList();
		boolean integrity = true;
		for(Block block : blockList) {
			if(!checkBlockHash(block)) {
				System.out.println("Bad hash for block "+block.getIndex());
				integrity = false;
			}
			if(!checkBlockPrevious(chain, block)) {
				System.out.println("Bad previous for block "+block.getIndex());
				integrity = false;
			}
		}
		return integrity;
	}
	
	/**
	 * Perform integrity check for a @Block. Only checks @Block's hash
	 * @param block the @Block to check
	 * @return true if @Block's hash is the one expected
	 */
	public static boolean checkBlockHash(Block block) {
		String hash = HashTools.calculateBlockHash(block);
		System.out.println("Check hash for "+block.getIndex()+". Expected : "+hash+", found : "+block.getHash());
		return hash.equals(block.getHash());
	}
	
	/**
	 * Perform integrity check for a @Block. Checks if @Block's parent is the one expected.
	 * @param block the @Block to check
	 * @return if @Block's parent is the one expected
	 */
	public static boolean checkBlockPrevious(ChainPart chain, Block block) {
		Block previous = findBlockByHash(chain, block.getPreviousHash());
		
		if(!block.isGenesis())
			System.out.println("Check previous for "+block.getIndex()+". Expected : "+(block.getIndex()-1)+", found : "+previous.getIndex());
		
		return block.isGenesis() || (previous != null && previous.getIndex() == block.getIndex() -1);
	}
}
