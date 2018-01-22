package com.avysel.blockchain.network.data.message;

public class CatchUpRequestMessage extends NetworkMessage {
	private long startIndex;

	public long getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(long startIndex) {
		this.startIndex = startIndex;
	}
	
}
