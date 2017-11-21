package com.avysel.blockchain.model;

import java.util.UUID;

public class SingleData {
	private String data;
	private UUID guid;

	public SingleData(String data) {
		this.data = data;
		this.guid = UUID.randomUUID();
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public UUID getGuid() {
		return guid;
	}
	
	public String toString() {
		return data + " - "+ guid.toString();
	}
}
