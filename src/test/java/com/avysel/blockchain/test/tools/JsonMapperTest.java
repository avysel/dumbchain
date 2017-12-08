package com.avysel.blockchain.test.tools;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.SingleData;
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
	
}
