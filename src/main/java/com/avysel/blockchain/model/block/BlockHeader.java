package com.avysel.blockchain.model.block;

/**
 * The header of a Block.
 * It contains all its metadata, used to identify a Block and link it to other Blocks
 */
public class BlockHeader {
	private long index;
	private String hash;
	private String previousHash;
	private String merkleRoot;
	private long timestamp;
	private long difficulty;
	
	public BlockHeader(String previousHash, String merkleRoot, long timestamp, long difficulty) {
		super();
		this.previousHash = previousHash;
		this.merkleRoot = merkleRoot;
		this.timestamp = timestamp;
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
	protected long getDifficulty() {
		return difficulty;
	}
	protected void setDifficulty(long difficulty) {
		this.difficulty = difficulty;
	}
}
