package com.avysel.blockchain.business.data;

import com.avysel.blockchain.business.data.custom.SingleData;
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
	
	Integer getOrder();
	void setOrder(Integer order);
	
	Long getTimestamp();
}
