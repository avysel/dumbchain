package com.avysel.blockchain.business.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.avysel.blockchain.business.BlockchainParameters;
import com.avysel.blockchain.business.data.ISingleData;
import com.avysel.blockchain.tools.JsonMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Block.
 * Contains a list of SingleData and all information used to identify the Block and check its integrity
 */
public class Block {
	
	private BlockHeader blockHeader;
	private BlockData blockData;
	
	public Block() {
		super();
		blockHeader = new BlockHeader();
		blockData = new BlockData();
	}
	
	/**
	 * Return if the block is the chain genesis.
	 * @return true if the block is the chain genesis, false otherwise
	 */
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
	 * Add a List of ISingleData to the Block.
	 * @param dataList the data to add
	 */ 
	public void addAllData(List<ISingleData> dataList) {
		this.getBlockData().getDataList().addAll(dataList);
	}
	
	/**
	 * Add a ISingleData to the Block.
	 * @param singleData the piece of data to add
	 */
	public void addData(ISingleData singleData) {
		this.getBlockData().getDataList().add(singleData);
	}
	
	/**
	 * Remove all data.
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
	public List<ISingleData> getDataList() {
		return this.getBlockData().getDataList();
	}	
	
	/**
	 * Return String representation of data used to calculate Block's hash.
	 * @return bytes representation of data
	 */
	@JsonIgnore
	public byte[] getHashData() {
		StringBuffer hashData = new StringBuffer();
		hashData.append(this.getDifficulty());
		hashData.append(this.getTimestamp());
		hashData.append(JsonMapper.dataListToJson(this.getDataList()));
		return hashData.toString().getBytes(BlockchainParameters.DEFAULT_CHARSET);
	}
	
	@JsonIgnore
	public String getStringData() { // only for debug display
		StringBuilder builder = new StringBuilder();
		
		builder.append("[");
		List<ISingleData> dataList = this.getDataList();
		for(ISingleData singleData : dataList) {
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
	
	/**
	 * Returns the quality of a block. 
	 * Quality is used when two blocks are in competition to be added to the chain, the one with higher quality will be added, the other one will be rejected.
	 * @return the quality of the block
	 */
	@JsonIgnore
	public long getQuality() {
		// TODO how to make it overridable or customisable ?
		return getDifficulty();
	}

	/**
	 * Returns the list of hashes of all block's data.
	 * @return list of data hashes
	 */
	@JsonIgnore
	public ArrayList<String> getDataHashesList() {
		ArrayList<String> list = new ArrayList<String>();

		Iterator<ISingleData> it = getDataList().iterator();
		while(it.hasNext()) {
			list.add(it.next().getHash());
		}
		
		return list;
	}	
	
	@Override
	public boolean equals(Object otherBlock) {
		
		if(!(otherBlock instanceof Block)) return false;
		
		return  this.getHash().equals(((Block)otherBlock).getHash())
				&& this.getIndex() == ((Block)otherBlock).getIndex()
				&& this.getTimestamp() == ((Block)otherBlock).getTimestamp()
				&& this.getMerkleRoot().equals(((Block)otherBlock).getMerkleRoot()
				);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(this);
	}
}
