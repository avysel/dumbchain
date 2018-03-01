package com.avysel.blockchain.business.data;

public interface IDataFactory {
	
	public ISingleData createData();
	
	public ISingleData createData(String serializedData);
	
	public ISingleData createData(String serializedData, Class clazz);
	
}
