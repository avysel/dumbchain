package com.avysel.blockchain.network.data.message;

import java.util.UUID;

public class NetworkMessage {
	private String uid;
	
	public String getUid() {
		return uid;
	}

	public NetworkMessage() {
		this.uid = UUID.randomUUID().toString();
	}
	
}
