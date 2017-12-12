package com.avysel.blockchain.tools;

import java.io.IOException;
import java.util.List;

import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.SingleData;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.network.peer.Peer;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);
		
		try {
			block = mapper.readValue(jsonData, Block.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return block;
	}

	public static String blockToJson(Block block) {
		String json = new String();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
		
		try {
			json = mapper.writeValueAsString(block);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return json;
	}

	public static SingleData jsonToData(String jsonData) {
		SingleData data = null;
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
		try {
			data = mapper.readValue(jsonData, SingleData.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	public static String dataToJson(SingleData data) {
		String json = new String();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
		
		try {
			json = mapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
				
		return json;
	}
	
	public static String dataListToJson(List<SingleData> dataList) {
		String json = new String();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
		
		try {
			json = mapper.writeValueAsString(dataList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
				
		return json;		
	}

	public static String bulkToJson(NetworkDataBulk bulk) {
		String json = new String();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
		
		try {
			json = mapper.writeValueAsString(bulk);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
				
		return json;		
	}	
	
	public static NetworkDataBulk jsonToBulk(String json) {
		NetworkDataBulk data = null;
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
		try {
			data = mapper.readValue(json, NetworkDataBulk.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;		
	}
	
	public static String peerToJson(Peer peer) {
		String json = new String();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
		
		try {
			json = mapper.writeValueAsString(peer);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
				
		return json;		
	}
	
	public static Peer jsonToPeer(String json) {
		Peer data = null;
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
		try {
			data = mapper.readValue(json, Peer.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;		
	}
}
