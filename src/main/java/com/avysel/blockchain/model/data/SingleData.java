package com.avysel.blockchain.model.data;

import java.util.UUID;

/**
 * A piece of data that can be included in a @Block.
 * A @Block can contains several @SingleData.
 */
public class SingleData implements ISingleData {
	private String data;
	private UUID uniqueId;
	
	public SingleData(String data) {
		this.data = data;
		this.uniqueId = UUID.randomUUID();
	}
	
	@Override
	public String getData() {
		return toString();
	}

	@Override
	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String getUniqueId() {
		return this.uniqueId.toString();
	}

	@Override
	public void setUniqueId(String uniqueId) {
		this.uniqueId = UUID.fromString(uniqueId);
	}	
	
	public String toString() {
		return data + " - "+ uniqueId.toString();
	}
}
