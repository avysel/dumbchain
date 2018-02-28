package com.avysel.blockchain.business.block;

import java.util.ArrayList;
import java.util.List;

import com.avysel.blockchain.business.data.ISingleData;

/**
 * The main content of Block.
 * It contains a List of ISingleData
 */
public class BlockData {
	
	// data list of the block
	private List<ISingleData> dataList;
	
	public BlockData() {
		super();
		dataList = new ArrayList<ISingleData>();
	}
		
	public BlockData(List<ISingleData> data) {
		super();
		dataList = new ArrayList<ISingleData>();
		dataList.addAll(data);
	}

	public List<ISingleData> getDataList() {
		return dataList;
	}

	public void setDataList(List<ISingleData> dataList) {
		this.dataList = dataList;
	}
	
}
