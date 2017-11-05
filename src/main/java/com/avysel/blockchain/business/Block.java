package com.avysel.blockchain.business;

public class Block {
	private BlockHeader header;
	private BlockData data;
	
	public BlockHeader getHeader() {
		return header;
	}
	public void setHeader(BlockHeader header) {
		this.header = header;
	}
	public BlockData getData() {
		return data;
	}
	public void setData(BlockData data) {
		this.data = data;
	}
	
	
}
