package com.avysel.blockchain.business.data.custom;

import com.avysel.blockchain.business.data.IDataFactory;
import com.avysel.blockchain.business.data.ISingleData;

public class DataFactory implements IDataFactory {

	@Override
	public ISingleData getDataInstance() {
		return new SingleData();
	}

	@Override
	public ISingleData getDataInstance(String serializedData, Class clazz) {
		return new SingleData(serializedData);
	}

}
