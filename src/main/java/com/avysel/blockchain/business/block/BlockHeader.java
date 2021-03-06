package com.avysel.blockchain.business.block;

/**
 * The header of a Block.
 * It contains all its metadata, used to identify a Block and link it to other Blocks
 */
public class BlockHeader {
	
	// the index of block in the chain
	private long index;
	
	// the hash of block, computed according the proof of work or similar
	private String hash;
	
	// the hash of previous block in chain
	private String previousHash;
	
	// merkle root of block data
	private String merkleRoot;
	
	// block creation timestamp
	private long timestamp;
	
	// number of tries to get a suitable hash for this block
	private long difficulty;
	
	// number of data in this block;
	private int dataCount;
	
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

	public int getDataCount() {
		return dataCount;
	}

	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}
	
}
