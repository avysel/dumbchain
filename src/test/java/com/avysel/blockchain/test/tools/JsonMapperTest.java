package com.avysel.blockchain.test.tools;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.SingleData;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.tools.JsonMapper;

public class JsonMapperTest {
	
	private final long index = 0;
	private final long difficulty = 520;
	private final String hash = "toto";
	private final String previousHash = "titi";
	private final long timestamp = 546546;
	private final SingleData data1 = new SingleData("data1");
	private final SingleData data2 = new SingleData("data2");
	
	private Block createTestBlock() {
		Block block = new Block();
		
		block.setDifficulty(difficulty);
		block.setHash(hash);
		block.setPreviousHash(previousHash);
		block.setIndex(index);
		block.setTimestamp(timestamp);
		block.addData(data1);
		block.addData(data2);
		
		return block;
	}
	
	@Test
	public void blockToJson() {
		Block block = createTestBlock();
		String json = JsonMapper.blockToJson(block);
		
		System.out.println("Json pour block : ");
		System.out.println(json);
		
		assertNotNull(json);
	}
	
	@Test
	public void jsonToBlock() {
		String json = "{\"timestamp\":546546,\"index\":0,\"difficulty\":520,\"hash\":\"toto\",\"previousHash\":\"titi\",\"dataList\":[{\"data\":\"data1\",\"uniqueId\":\"357b8e96-a6d2-47f1-9ec6-11fa1b1599d4\"},{\"data\":\"data2\",\"uniqueId\":\"44e53fd3-6ecb-40a7-9a28-f6708a587328\"}],\"merkleRoot\":null}";
	
		Block block = JsonMapper.jsonToBlock(json);
		System.out.println("Block pour json : ");
		System.out.println(block);
		
		assertEquals(block.getHash(), hash);
		assertEquals(block.getPreviousHash(), previousHash);
		assertEquals(block.getDifficulty(), difficulty);
		assertEquals(block.getIndex(), index);
		assertEquals(block.getTimestamp(), timestamp);
		assertEquals(block.getDataList().size(), 2);
	}
	
	@Test
	public void dataToJson() {
		String json = JsonMapper.dataToJson(data1);
		System.out.println("Json pour data ");
		System.out.println(json);	
		
		assertNotNull(json);
	}
	
	@Test
	public void jsonToData() {
		String json = "{\"data\":\"data1\",\"uniqueId\":\"2cb9e2a9-fa4b-489c-914a-fa6dffce88fd\"}";
		SingleData data = JsonMapper.jsonToData(json);
		
		System.out.println("Data pour json : ");
		System.out.println(data);
		
		assertNotNull(data.getData());
		assertEquals(data.getData(), "data1");
		assertNotNull(data.getUniqueId());
		assertEquals(data.getUniqueId(), "2cb9e2a9-fa4b-489c-914a-fa6dffce88fd");
	}

	@Test
	public void jsonToDataNoId() {
		String json = "{\"data\":\"data1\"}";
		SingleData data = JsonMapper.jsonToData(json);
		
		System.out.println("Data pour json no id : ");
		System.out.println(data);
		
		assertNotNull(data.getData());
		assertEquals(data.getData(), "data1");
		assertNull(data.getUniqueId());
	}
	
	@Test
	public void dataListToJson() {
		List<SingleData> list = new ArrayList<SingleData>();
		list.add(data1);
		list.add(data2);
		
		String json = JsonMapper.dataListToJson(list);
		
		assertNotNull(json);
		System.out.println("Json pour datalist : ");
		System.out.println(json);
	}

	@Test
	public void bulkToJson() {
		
		NetworkDataBulk bulk = new NetworkDataBulk();
		bulk.setBulkType(NetworkDataBulk.DATATYPE_BLOCK);
		bulk.setBulkData(JsonMapper.blockToJson(createTestBlock()));
		
		String json = JsonMapper.bulkToJson(bulk);
		
		assertNotNull(json);
		System.out.println("Json pour bulk : ");
		System.out.println(json);
	}

	@Test
	public void jsonToBulk() {
		
		String json = "{\"type\":101,\"data\":\"{\\\"timestamp\\\":546546,\\\"index\\\":0,\\\"difficulty\\\":520,\\\"hash\\\":\\\"toto\\\",\\\"previousHash\\\":\\\"titi\\\",\\\"dataList\\\":[{\\\"data\\\":\\\"data1\\\",\\\"uniqueId\\\":\\\"4f65c8a8-4502-4b87-a9ca-78d2e6f05265\\\"},{\\\"data\\\":\\\"data2\\\",\\\"uniqueId\\\":\\\"9af6a263-d0f4-4849-9ff7-1a4b6212b73a\\\"}],\\\"merkleRoot\\\":null}\"}";
		
		NetworkDataBulk bulk = JsonMapper.jsonToBulk(json);
		
		assertNotNull(bulk);
		System.out.println("Bulk pour json : ");
		System.out.println(bulk);
		
		assertNotNull(bulk.getBulkType());
		assertEquals(bulk.getBulkType(), NetworkDataBulk.DATATYPE_BLOCK);
		assertNotNull(bulk.getBulkData());
	}	
	
}
