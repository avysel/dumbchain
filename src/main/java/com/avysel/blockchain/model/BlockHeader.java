package com.avysel.blockchain.model;

public class BlockHeader {
	private long index;
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
	
	protected BlockHeader() {
		super();
	}

	protected long getIndex() {
		return index;
	}
	protected void setIndex(long index) {
		this.index = index;
	}
	protected String getHash() {
		return hash;
	}
	protected void setHash(String hash) {
		this.hash = hash;
	}
	protected String getPreviousHash() {
		return previousHash;
	}
	protected void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}
	protected String getMerkleRoot() {
		return merkleRoot;
	}
	protected void setMerkleRoot(String merkleRoot) {
		this.merkleRoot = merkleRoot;
	}
	protected long getTimestamp() {
		return timestamp;
	}
	protected void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	protected long getNonce() {
		return nonce;
	}
	protected void setNonce(long nonce) {
		this.nonce = nonce;
	}
	protected long getDifficulty() {
		return difficulty;
	}
	protected void setDifficulty(long difficulty) {
		this.difficulty = difficulty;
	}
	
	
}
