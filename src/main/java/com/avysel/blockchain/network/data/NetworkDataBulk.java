package com.avysel.blockchain.network.data;

import com.avysel.blockchain.tools.JsonMapper;

/**
 * Encapsulate a data to broadcast to the network and the type of data.
 *
 */
public class NetworkDataBulk {
	/**
	 * This bulk contains a data.
	 */
	public static final int DATATYPE_DATA = 100;
	/**
	 * This bulk contains a block.
	 */
	public static final int DATATYPE_BLOCK = 101;
	/**
	 * This bulk contains a chain part.
	 */
	public static final int DATATYPE_CHAIN = 102;	

	/**
	 * This bulk contains an message from a new node announcing its arrival on network.
	 */
	public static final int MESSAGE_PEER_HELLO = 300;
	
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
	
	public String toString() {
		return JsonMapper.bulkToJson(this);
	}
}
