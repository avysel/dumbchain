package com.avysel.blockchain.tools;

import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.ISingleData;
import com.avysel.blockchain.model.data.SingleData;

public class JsonMapper {
	
	/**
	 * @param block
	 * @return
	 */
	public String toJson(Block block) {
		return null;
	}
	
	public static Block jsonToBlock(String jsonData) {
		Block block = new Block();
		
		// TODO
		
		return block;
	}

	public static String blockToJson(Block block) {
		String json = new String();
		
		// TODO
		
		return json;
	}

	public static ISingleData jsonToData(String jsonData) {
		ISingleData data = new SingleData(null);
		
		// TODO
		
		return data;
	}

	public static String dataToJson(ISingleData data) {
		String json = new String();
		
		// TODO
		
		return json;
	}	
	
}
