package com.avysel.blockchain.business.data.custom;

import java.util.TimeZone;
import java.util.UUID;

import com.avysel.blockchain.business.BlockchainParameters;
import com.avysel.blockchain.business.data.ISingleData;
import com.avysel.blockchain.crypto.HashTools;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A piece of data that can be included in a Block.
 * A Block can contains several SingleData.
 */
public class SingleData implements ISingleData {
	private String data;
	private String uuid;
	private String hash;
	private Class clazz;
	private Integer order;
	private Long timestamp;

	public SingleData() {
		super();
	}

	public SingleData(String data) {
		this.data = data;
		this.uuid = UUID.randomUUID().toString();
		this.hash = HashTools.calculateHash((data+uuid).getBytes(BlockchainParameters.DEFAULT_CHARSET));
		
		// all block creation timestamps are based on GMT+0 timezone
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		this.timestamp = System.currentTimeMillis();
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

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@JsonIgnore
	public String toString() {
		if(hash != null)
			return data + " - " + hash;
		else
			return data;

	}
}
