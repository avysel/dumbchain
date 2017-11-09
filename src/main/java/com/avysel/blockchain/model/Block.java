package com.avysel.blockchain.model;

import java.util.List;

import com.avysel.blockchain.model.BlockData;
import com.avysel.blockchain.model.BlockHeader;

public class Block {
	
	private BlockHeader blockHeader;
	private BlockData blockData;
	
	public Block() {
		super();
	}
	
	public Block(BlockHeader header, BlockData data) {
		super();
		this.blockData = data;
		this.blockHeader = header;
	}
	
	protected BlockHeader getBlockHeader() {
		return blockHeader;
	}
	
	public void setBlockHeader(BlockHeader blockHeader) {
		this.blockHeader = blockHeader;
	}
	
	protected BlockData getBlockData() {
		return blockData;
	}
	public void setBlockData(BlockData blockData) {
		this.blockData = blockData;
	}
	
	public String getPreviousHash() {
		return this.getBlockHeader().getPreviousHash();
	}
	
	public String getHash() {
		return this.getBlockHeader().getHash();
	}

	public void setPreviousHash(String hash) {
		this.getBlockHeader().setPreviousHash(hash);
	}	

	public List<SingleData> getDataList() {
		return this.getBlockData().getDataList();
	}	
	
}
