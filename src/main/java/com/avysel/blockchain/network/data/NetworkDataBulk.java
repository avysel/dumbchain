package com.avysel.blockchain.network.data;

import java.util.UUID;

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
	 * This bulk contains an message from a new node announcing its arrival on network.
	 */
	public static final int MESSAGE_PEER_HELLO = 300;

	/**
	 * This bulk contains an answer to a new node hello message, to give back our current ip.port.
	 */
	public static final int MESSAGE_PEER_HELLO_ANSWER = 301;
	
	/**
	 * This bulk contains an message from a new node requesting catch up of data it doesn't has
	 */
	public static final int MESSAGE_CATCH_UP_REQUEST = 302;
	
	/**
	 * This bulk contains an message from a node sending data to a new node for catch-up
	 */
	public static final int MESSAGE_CATCH_UP_BLOCKS = 303;	
	
	/**
	 * This bulk contains an message from a node send to a new node that there is nothing to catch-up
	 */
	public static final int MESSAGE_CATCH_UP_EMPTY = 304;
	
	private String uid;
	private int bulkType;
	private String bulkData;
	
	private Peer sender;

	public NetworkDataBulk() {
		super();
		this.uid = UUID.randomUUID().toString();
	}	
	
	public NetworkDataBulk(int bulkType, String bulkData) {
		this();
		this.bulkType = bulkType;
		this.bulkData = bulkData;
	}
	
	public String getUid() {
		return uid;
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
