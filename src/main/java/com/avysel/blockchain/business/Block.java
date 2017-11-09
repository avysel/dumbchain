package com.avysel.blockchain.business;

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
	
	public BlockHeader getBlockHeader() {
		return blockHeader;
	}
	public void setBlockHeader(BlockHeader blockHeader) {
		this.blockHeader = blockHeader;
	}
	public BlockData getBlockData() {
		return blockData;
	}
	public void setBlockData(BlockData blockData) {
		this.blockData = blockData;
	}
	
}
