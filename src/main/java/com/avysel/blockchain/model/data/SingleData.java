package com.avysel.blockchain.model.data;

import java.util.UUID;

import com.avysel.blockchain.crypto.HashTools;

/**
 * A piece of data that can be included in a @Block.
 * A @Block can contains several @SingleData.
 */
public class SingleData {
	private String data;
	private String uuid;
	private String hash;

	public SingleData() {
		super();
	}

	public SingleData(String data) {
		this.data = data;
		this.uuid = UUID.randomUUID().toString();
		this.hash = HashTools.calculateHash(data+uuid);
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String toString() {
		if(hash != null)
			return data + " - " + hash;
		else
			return data;

	}
}
