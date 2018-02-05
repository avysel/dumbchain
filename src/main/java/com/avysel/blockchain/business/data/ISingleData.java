package com.avysel.blockchain.business.data;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,  
		property = "clazz",
		defaultImpl=SingleData.class
		)
public interface ISingleData {

	String getData();

	String getHash();

	Class getClazz();

	String toString();
}
