package com.avysel.blockchain.model.chain;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.block.Genesis;
import com.avysel.blockchain.model.data.ISingleData;

/**
 * The blockchain.
 * It contains the list of @Block, starting with a @Genesis
 */
public class Chain extends ChainPart {

	public Chain() {
		super();
	}

	public Block getGenesis() {
		if(getFirstBlock().isGenesis())
			return getFirstBlock();
		else
			return null;
	}

	public void setGenesis(Block genesis) {
		synchronized(getBlockList()) {
			getBlockList().addFirst(genesis);
		}
	}

	@Override
	public long getLastIndex() {
		if(this.getLastBlock() != null) {
			return this.getLastBlock().getIndex();
		}
		else {
			return Genesis.GENESIS_INDEX;
		}
	}	

	/**
	 * Add a new @Block to the @Chain, and set @Block link data (previous hash ...). It also manages to add a @Block if it is the @Genesis @Block
	 * @param block the @Block to add.
	 */
	@Override
	public void linkBlock(Block block) {
		synchronized(getBlockList()) {
			if( ! block.isGenesis()) {
				block.setPreviousHash(getLastBlock().getHash());
				block.setIndex(getLastIndex() + 1);
			}

			getBlockList().add(block);
		}
	}

	public List<ISingleData> unlinkBlock(long startIndex) {

		synchronized (getBlockList()) {

			List<ISingleData> dataList = new LinkedList<ISingleData>();

			Iterator<Block> lit = getBlockList().descendingIterator();
			while(lit.hasNext()){
				Block block = lit.next();
				if(block.getIndex() >= startIndex) {
					dataList.addAll(block.getDataList());
					getBlockList().remove(block);
				}
				else {
					break;
				}
			}

			return dataList;
		}
	}
}
