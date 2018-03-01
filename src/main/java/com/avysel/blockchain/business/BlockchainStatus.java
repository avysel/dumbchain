package com.avysel.blockchain.business;

public class BlockchainStatus {
	private String nodeId;
	private long height;
	private String localPeer;
	private boolean mining;
	
	public BlockchainStatus() {
		super();
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public long getHeight() {
		return height;
	}
	public void setHeight(long height) {
		this.height = height;
	}
	public String getLocalPeer() {
		return localPeer;
	}
	public void setLocalPeer(String localPeer) {
		this.localPeer = localPeer;
	}
	public boolean isMining() {
		return mining;
	}
	public void setMining(boolean mining) {
		this.mining = mining;
	}
}
