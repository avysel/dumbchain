package com.avysel.blockchain.model.block;

import java.util.ArrayList;
import java.util.List;

import com.avysel.blockchain.model.data.ISingleData;
import com.avysel.blockchain.model.data.SingleData;

/**
 * The main content of Block.
 * It contains a List of ISingleData
 */
public class BlockData {
	
	// data list if the block
	private List<ISingleData> dataList;

	public BlockData() {
		super();
		dataList = new ArrayList<ISingleData>();
	}
		
	public BlockData(List<String> data) {
		super();
		dataList = new ArrayList<ISingleData>();
		for(String s : data) {
			dataList.add(new SingleData(s));
		}
	}	

	public List<ISingleData> getDataList() {
		return dataList;
	}

	public void setDataList(List<ISingleData> dataList) {
		this.dataList = dataList;
	}
	
}
