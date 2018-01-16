package com.avysel.blockchain.model.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.avysel.blockchain.model.data.SingleData;
import com.avysel.blockchain.tools.JsonMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
/*
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
*/
/**
 * A @Block
 * Contains a list of @SingleData and all information used to identify the @Block and check its integrity
 */
/**
 * @author avanryssel
 *
 */
public class Block {
	
	protected BlockHeader blockHeader;
	protected BlockData blockData;
	
	public Block() {
		super();
		blockHeader = new BlockHeader();
		blockData = new BlockData();
	}
	
	@JsonIgnore
	public boolean isGenesis() {
		return false;
	}
	
	protected BlockHeader getBlockHeader() {
		return blockHeader;
	}
	
	protected BlockData getBlockData() {
		return blockData;
	}

	/**
	 * Add a @List of @SingleData to the @Block
	 * @param dataList the data to add
	 */ 
	public void addAllData(List<SingleData> dataList) {
		this.getBlockData().getDataList().addAll(dataList);
	}
	
	/**
	 * Add a @SingleData to the @Block
	 * @param singleData the piece of data to add
	 */
	public void addData(SingleData singleData) {
		this.getBlockData().getDataList().add(singleData);
	}
	
	/**
	 * Remove all data 
	 */
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
	@JsonIgnore
	public String getHashData() {
		// TODO move to byte[] ?
		// TODO use header data instead of data ?
		StringBuffer hashData = new StringBuffer();
		//hashData.append(this.getIndex());
		//hashData.append(this.getTimestamp());
		hashData.append(JsonMapper.dataListToJson(this.getDataList()));
		return hashData.toString();
	}
	
	/**
	 * Return string representation of data used to calculate block's merkle root
	 * @return
	 */
	@JsonIgnore	
	public String getMerkleRootData() {
		// TODO move to byte[] ?
		StringBuffer hashData = new StringBuffer();
		hashData.append(JsonMapper.dataListToJson(this.getDataList()));
		return hashData.toString();		
	}
	
	@JsonIgnore
	public String getStringData() { // only for debug display
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
	
	public String toString() { // only for debug display
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
		builder.append(", difficulty:");
		builder.append(this.getDifficulty());
		builder.append("}");
		
		return builder.toString();
	}
	
	public boolean equals(Block otherBlock) {
		return this.getHash().equals(otherBlock.getHash());
	}
	
	/**
	 * Returns the quality of a block. 
	 * Quality is used when two blocks are in competition to be added to the chain, the one with higher quality will be added, the other one will be rejected.
	 * @return
	 */
	public long getQuality() {
		// TODO how to make it overridable or customisable ?
		return getDifficulty();
	}

	@JsonIgnore
	public ArrayList<String> getDataHashesList() {
		ArrayList<String> list = new ArrayList<String>();

		Iterator<SingleData> it = getDataList().iterator();
		while(it.hasNext()) {
			list.add(it.next().getHash());
		}
		
		return list;
	}	
	
}
