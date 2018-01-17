package com.avysel.blockchain.network.data;

import com.avysel.blockchain.network.peer.Peer;
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

	/**
	 * This bulk contains an answer to a new node hello message, to give back our current ip.port.
	 */
	public static final int MESSAGE_PEER_HELLO_ANSWER = 301;
	
	private int bulkType;
	private String bulkData;
	
	private Peer sender;

	public NetworkDataBulk() {
		super();
	}	
	
	public NetworkDataBulk(int bulkType, String bulkData) {
		super();
		this.bulkType = bulkType;
		this.bulkData = bulkData;
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

	public Peer getSender() {
		return sender;
	}

	public void setSender(Peer sender) {
		this.sender = sender;
	}

	public String toString() {
		return JsonMapper.bulkToJson(this);
	}
}
