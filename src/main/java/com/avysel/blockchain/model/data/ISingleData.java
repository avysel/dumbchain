package com.avysel.blockchain.model.data;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		defaultImpl=SingleData.class
	)
public interface ISingleData {
	
	public String getData();

	public String getHash();

	public Class getClazz();

	public String toString();
}
