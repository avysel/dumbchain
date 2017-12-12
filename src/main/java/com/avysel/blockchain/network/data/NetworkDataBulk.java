package com.avysel.blockchain.network.data;

/**
 * Encapsulate a data to broadcast to the network and the type of data.
 *
 */
public class NetworkDataBulk {
	public static final int DATATYPE_DATA = 100;
	public static final int DATATYPE_BLOCK = 101;
	public static final int DATATYPE_CHAIN = 102;	
	
	public static final int MESSAGE_REQUEST_CONNECTION = 200;
	
	private int type;
	private String data;

	public NetworkDataBulk() {
		super();
	}	
	
	public NetworkDataBulk(int type, String data) {
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
