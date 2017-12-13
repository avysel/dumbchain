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
	
	private int bulkType;
	private String bulkData;

	public NetworkDataBulk() {
		super();
	}	
	
	public NetworkDataBulk(int type, String data) {
		super();
		this.bulkType = type;
		this.bulkData = data;
	}
	
	public int getBulkType() {
		return bulkType;
	}
	
	public void setBulkType(int bulkType) {
		this.bulkType = bulkType;
	}
	
	public String getBulkData() {
		return bulkData;
	}
	
	public void setBulkData(String bulkData) {
		this.bulkData = bulkData;
	}
	
	public String toString() {
		return JsonMapper.bulkToJson(this);
	}
}
