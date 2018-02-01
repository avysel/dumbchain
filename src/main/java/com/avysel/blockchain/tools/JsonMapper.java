package com.avysel.blockchain.tools;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.ISingleData;
import com.avysel.blockchain.model.data.SingleData;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.network.data.message.CatchUpDataMessage;
import com.avysel.blockchain.network.data.message.CatchUpRequestMessage;
import com.avysel.blockchain.network.data.message.NetworkMessage;
import com.avysel.blockchain.network.peer.Peer;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class JsonMapper {

	private static Logger log = Logger.getLogger(JsonMapper.class);

	private JsonMapper() {}
	
	public static String genericToJson(Object o) {
		String json = new String();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);

		try {
			json = mapper.writeValueAsString(o);
			log.trace("Serialized block : "+json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return json;
	}

	public static Object jsonToGeneric(String jsonData, Class clazz) {
		Object object;
		try {
			object = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();
			return null;
		}

		if(jsonData == null || jsonData.isEmpty()) return null;

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);
		mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);

		try {
			object = mapper.readValue(jsonData, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return object;

	}

	public static String blockListToJson(List<Block> list) {
		return genericToJson(list);
	}

	public static List<Block> jsonToBlockList(String jsonData) {
		List<Block> list = new LinkedList<Block>();

		if(jsonData == null || jsonData.isEmpty()) return null;

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);
		mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);

		try {
			list = mapper.readValue(jsonData, mapper.getTypeFactory().constructCollectionType(LinkedList.class, Block.class));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}	

	public static Block jsonToBlock(String jsonData) {
		Block block = new Block();

		if(jsonData == null || jsonData.isEmpty()) return null;

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);
		mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);

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
		return genericToJson(block);
	}

	public static SingleData jsonToData(String jsonData) {
		SingleData data = null;

		if(jsonData == null || jsonData.isEmpty()) return null;

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

	public static String dataToJson(ISingleData data) {
		return genericToJson(data);
	}

	public static String dataListToJson(List<ISingleData> dataList) {
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
		return genericToJson(bulk);
	}	

	public static NetworkDataBulk jsonToBulk(String jsonData) {
		NetworkDataBulk dataBulk = null;

		if(jsonData == null || jsonData.isEmpty()) return null;

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
		try {
			dataBulk = mapper.readValue(jsonData, NetworkDataBulk.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.trace("JSON : "+jsonData);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataBulk;		
	}

	public static String peerToJson(Peer peer) {
		return genericToJson(peer);
	}

	public static Peer jsonToPeer(String jsonData) {
		Peer data = null;

		if(jsonData == null || jsonData.isEmpty()) return null;

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
		try {
			data = mapper.readValue(jsonData, Peer.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;		
	}

	public static String messageToJson(NetworkMessage message) {
		return genericToJson(message);
	}

	public static CatchUpRequestMessage jsonToCatchUpRequestMessage(String jsonData) {
		CatchUpRequestMessage message = null;

		if(jsonData == null || jsonData.isEmpty()) return null;

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
		try {
			message =  mapper.readValue(jsonData, CatchUpRequestMessage.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return message;			
	}
	
	public static CatchUpDataMessage jsonToCatchUpDataMessage(String jsonData) {
		return (CatchUpDataMessage) jsonToGeneric(jsonData, CatchUpDataMessage.class);
	}

}
