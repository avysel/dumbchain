package com.avysel.blockchain.model;

import java.util.ArrayList;
import java.util.List;

public class BlockData {
	private String data;
	
	private String identifier;
	private List<SingleData> dataList = new ArrayList<SingleData>();

	public BlockData() {
		super();
	}
	
	public BlockData(String data) {
		super();
		this.data = data;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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
