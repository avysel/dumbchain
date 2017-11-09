package com.avysel.blockchain.model;

public class BlockHeader {
	private long height;
	private String hash;
	private String previousHash;
	private String merkleRoot;
	private long timestamp;
	private long nonce;
	private long difficulty;
	
	public BlockHeader(String previousHash, String merkleRoot, long timestamp, long nonce, long difficulty) {
		super();
		this.previousHash = previousHash;
		this.merkleRoot = merkleRoot;
		this.timestamp = timestamp;
		this.nonce = nonce;
		this.difficulty = difficulty;
	}
	
	public BlockHeader() {
		super();
	}

	public long getHeight() {
		return height;
	}
	public void setHeight(long height) {
		this.height = height;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getPreviousHash() {
		return previousHash;
	}
	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}
	public String getMerkleRoot() {
		return merkleRoot;
	}
	public void setMerkleRoot(String merkleRoot) {
		this.merkleRoot = merkleRoot;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public long getNonce() {
		return nonce;
	}
	public void setNonce(long nonce) {
		this.nonce = nonce;
	}
	public long getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(long difficulty) {
		this.difficulty = difficulty;
	}
	
	
}
