package com.avysel.blockchain.model.data;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		defaultImpl=SingleData.class
	)
public interface ISingleData {
	
	String getData();

	String getHash();

	Class getClazz();

	String toString();
}
