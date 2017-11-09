package com.avysel.blockchain.model;

import java.util.ArrayList;
import java.util.List;

public class BlockData {
	
	private String identifier;
	private List<SingleData> dataList;

	public BlockData() {
		super();
	}
		
	public BlockData(List<String> data) {
		super();
		dataList = new ArrayList<SingleData>();
		for(String s : data) {
			dataList.add(new SingleData(s));
		}
	}	

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public List<SingleData> getDataList() {
		return dataList;
	}

	public void setDataList(List<SingleData> dataList) {
		this.dataList = dataList;
	}
	
}
