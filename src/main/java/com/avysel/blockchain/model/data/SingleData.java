package com.avysel.blockchain.model.data;

import java.util.UUID;

/**
 * A piece of data that can be included in a @Block.
 * A @Block can contains several @SingleData.
 */
public class SingleData implements ISingleData {
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

	@Override
	public String jsonData() {
		
		return toString();
	}
}
