package com.avysel.blockchain.model;

import java.util.List;
/*
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
*/
/**
 * A @Block
 * Contains a list of @SingleData and all information used to identify the @Block and check its integrity
 */
public class Block {
	
	private BlockHeader blockHeader;
	private BlockData blockData;
	
	public Block() {
		super();
		blockHeader = new BlockHeader();
		blockData = new BlockData();
	}
	
	public boolean isGenesis() {
		return this.getIndex() == Chain.GENESIS_INDEX;
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
	public void cleanData() {
		this.getBlockData().getDataList().clear();
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
	public long getDifficulty() {
		return this.getBlockHeader().getDifficulty();
	}
	public void setDifficulty(long difficulty) {
		this.getBlockHeader().setDifficulty(difficulty);
	}	
	
	public List<SingleData> getDataList() {
		return this.getBlockData().getDataList();
	}	
	
	/**
	 * Return @String representation of data used to calculate @Block's hash
	 * @return
	 */
	public String getHashData() {
		StringBuffer hashData = new StringBuffer();
		//hashData.append(this.getIndex());
		//hashData.append(this.getTimestamp());
		hashData.append(this.getDataList()); // TODO passer en json
		return hashData.toString();
	}
	
	public String getStringData() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("[");
		List<SingleData> dataList = this.getDataList();
		for(SingleData singleData : dataList) {
			builder.append(singleData.getData());
			builder.append(", ");
		}
		builder.append("]");
		return builder.toString();
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("{index:");
		builder.append(this.getIndex());
		builder.append(", time:");
		builder.append(this.getTimestamp());
		builder.append(", data:");
		builder.append(this.getStringData());
		builder.append(", hash:");
		builder.append(this.getHash());
		builder.append(", previousHash:");
		builder.append(this.getPreviousHash());
		builder.append("}");
		
		return builder.toString();
	}
	
	/*public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}*/
}
