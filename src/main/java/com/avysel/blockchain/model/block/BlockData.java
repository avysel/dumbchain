package com.avysel.blockchain.model.block;

import java.util.ArrayList;
import java.util.List;

import com.avysel.blockchain.model.data.SingleData;

/**
 * The main content of of @Block.
 * It contains a @List of @SingleData
 */
public class BlockData {
	
	private List<SingleData> dataList;

	public BlockData() {
		super();
		dataList = new ArrayList<SingleData>();
	}
		
	public BlockData(List<String> data) {
		super();
		dataList = new ArrayList<SingleData>();
		for(String s : data) {
			dataList.add(new SingleData(s));
		}
	}	

	public List<SingleData> getDataList() {
		return dataList;
	}

	public void setDataList(List<SingleData> dataList) {
		this.dataList = dataList;
	}
	
}
