package com.avysel.blockchain.model;

public class BlockData {
	private String data;

	public BlockData() {
		super();
	}
	
	public BlockData(String data) {
		super();
		this.data = data;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
