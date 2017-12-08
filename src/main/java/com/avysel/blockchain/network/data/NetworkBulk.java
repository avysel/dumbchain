package com.avysel.blockchain.network.data;

public class NetworkBulk {
	public static final int DATATYPE_DATA = 100;
	public static final int DATATYPE_BLOCK = 101;
	public static final int DATATYPE_CHAIN = 102;	
	
	private int type;
	private String data;

	public NetworkBulk() {
		super();
	}	
	
	public NetworkBulk(int type, String data) {
		super();
		this.type = type;
		this.data = data;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
}
