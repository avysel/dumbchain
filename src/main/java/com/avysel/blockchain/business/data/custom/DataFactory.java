package com.avysel.blockchain.business.data.custom;

import com.avysel.blockchain.business.data.IDataFactory;
import com.avysel.blockchain.business.data.ISingleData;

public class DataFactory implements IDataFactory {

	@Override
	public ISingleData createData() {
		return new SingleData();
	}

	@Override
	public ISingleData createData(String serializedData) {
		SingleData data = new SingleData(serializedData);
		data.setClazz(MyCustomObject.class);
		return data;
	}	
	
	@Override
	public ISingleData createData(String serializedData, Class clazz) {
		SingleData data = new SingleData(serializedData);
		data.setClazz(clazz);
		return data;
	}

}
