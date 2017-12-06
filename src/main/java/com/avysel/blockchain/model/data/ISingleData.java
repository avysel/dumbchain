package com.avysel.blockchain.model.data;

/**
 * Interface for a piece of data that can be included in a @Block.
 * A @Block can contains several @ISingleData.
 * To use a custom data object, implements this Interface.
 */
public interface ISingleData {

	/**
	 * Return JSON representation of the data
	 * @return JSON string
	 */
	public String jsonData();
	
}
