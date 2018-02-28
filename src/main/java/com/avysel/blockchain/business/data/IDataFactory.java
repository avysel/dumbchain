package com.avysel.blockchain.business.data;

public interface IDataFactory {
	
	public ISingleData getDataInstance();
	
	public ISingleData getDataInstance(String serializedData, Class clazz);
}
