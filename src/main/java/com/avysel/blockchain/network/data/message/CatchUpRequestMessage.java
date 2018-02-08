package com.avysel.blockchain.network.data.message;

public class CatchUpRequestMessage extends NetworkMessage {
	private long lastIndex;

	public long getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(long startIndex) {
		this.lastIndex = startIndex;
	}
	
}
