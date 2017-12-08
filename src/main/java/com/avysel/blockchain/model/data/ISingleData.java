package com.avysel.blockchain.model.data;

/**
 * Interface for a piece of data that can be included in a @Block.
 * A @Block can contains several @ISingleData.
 * To use a custom data object, implements this Interface.
 */
public interface ISingleData {
	
	/**
	 * Returns JSON representation of the data.
	 * @return JSON string
	 */
	public String getData();
	
	public void setData(String data);
	
	/**
	 * Returns the unique identifier for the data.
	 * @return a unique identifier
	 */
	public String getUniqueId();
	
	public void setUniqueId(String uniqueId);

}
