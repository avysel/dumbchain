package com.avysel.blockchain.model;

import java.util.List;

import com.avysel.blockchain.model.BlockData;
import com.avysel.blockchain.model.BlockHeader;

public class Block {
	
	private BlockHeader blockHeader;
	private BlockData blockData;
	
	public Block() {
		super();
		blockHeader = new BlockHeader();
		blockData = new BlockData();
	}
	
	private BlockHeader getBlockHeader() {
		return blockHeader;
	}
	
	/*private void setBlockHeader(BlockHeader blockHeader) {
		this.blockHeader = blockHeader;
	}*/
	
	private BlockData getBlockData() {
		return blockData;
	}
	/*private void setBlockData(BlockData blockData) {
		this.blockData = blockData;
	}*/
	public void addData(SingleData singleData) {
		this.getBlockData().getDataList().add(singleData);
	}
	public String getPreviousHash() {
		return this.getBlockHeader().getPreviousHash();
	}
	
	public String getHash() {
		return this.getBlockHeader().getHash();
	}
	public void setHash(String hash) {
		this.getBlockHeader().setHash(hash);
	}
	
	public void setPreviousHash(String hash) {
		this.getBlockHeader().setPreviousHash(hash);
	}	

	public long getIndex() {
		return this.getBlockHeader().getIndex();
	}
	public void setIndex(long index) {
		this.getBlockHeader().setIndex(index);
	}

	public String getMerkleRoot() {
		return this.getBlockHeader().getMerkleRoot();
	}
	public void setMerkleRoot(String merkleRoot) {
		this.getBlockHeader().setMerkleRoot(merkleRoot);
	}
	public long getTimestamp() {
		return this.getBlockHeader().getTimestamp();
	}
	public void setTimestamp(long timestamp) {
		this.getBlockHeader().setTimestamp(timestamp);
	}
	public long getNonce() {
		return this.getBlockHeader().getNonce();
	}
	public void setNonce(long nonce) {
		this.getBlockHeader().setNonce(nonce);
	}
	public long getDifficulty() {
		return this.getBlockHeader().getDifficulty();
	}
	public void setDifficulty(long difficulty) {
		this.getBlockHeader().setDifficulty(difficulty);
	}	
	
	public List<SingleData> getDataList() {
		return this.getBlockData().getDataList();
	}	
	
	public String getHashData() {
		StringBuffer hashData = new StringBuffer();
		hashData.append(this.getIndex());
		hashData.append(this.getTimestamp());
		hashData.append(this.getDataList()); // TODO passer en json
		return hashData.toString();
	}
}
