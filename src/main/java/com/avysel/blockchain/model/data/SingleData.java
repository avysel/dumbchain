package com.avysel.blockchain.model.data;

import java.util.UUID;

/**
 * A piece of data that can be included in a @Block.
 * A @Block can contains several @SingleData.
 */
public class SingleData/* implements ISingleData */{
	private String data;
	private UUID uniqueId;

	public SingleData() {
		super();
	}

	public SingleData(String data) {
		this.data = data;
		this.uniqueId = UUID.randomUUID();
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getUniqueId() {
		if(uniqueId != null)
			return this.uniqueId.toString();
		else
			return null;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = UUID.fromString(uniqueId);
	}	

	public String toString() {
		if(uniqueId != null)
			return data + " - " + uniqueId.toString();
		else
			return data;

	}
}
