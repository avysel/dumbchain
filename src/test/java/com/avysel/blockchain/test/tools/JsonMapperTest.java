package com.avysel.blockchain.test.tools;

import org.junit.Test;

import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.ISingleData;
import com.avysel.blockchain.model.data.SingleData;
import com.avysel.blockchain.tools.JsonMapper;

public class JsonMapperTest {
	
	private final long index = 0;
	private final long difficulty = 520;
	private final String hash = "toto";
	private final String previousHash = "titi";
	private final long timestamp = 546546;
	private final ISingleData data1 = new SingleData("data1");
	private final ISingleData data2 = new SingleData("data2");
	
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
		
		System.out.println("Json pour block : ");
		System.out.println(JsonMapper.blockToJson(block));
	}
	
	@Test
	public void jsonToBlock() {
		String json = "{\"timestamp\":546546,\"index\":0,\"difficulty\":520,\"hash\":\"toto\",\"previousHash\":\"titi\",\"hashData\":\"[]\",\"stringData\":\"[]\",\"merkleRoot\":null,\"genesis\":false,\"dataList\":[]}";
	
		Block block = JsonMapper.jsonToBlock(json);
		System.out.println("Block pour json : ");
		System.out.println(block);
	}
	
	@Test
	public void dataToJson() {
		String json = JsonMapper.dataToJson(data1);
		System.out.println("Json pour data ");
		System.out.println(json);
	}
	
	@Test
	public void jsonToData() {
		String json = "{\"data\":\"data1\",\"uniqueId\":\"2cb9e2a9-fa4b-489c-914a-fa6dffce88fd\"}";
		ISingleData data = JsonMapper.jsonToData(json);
		
		System.out.println("Data pour json : ");
		System.out.println(data);
	}
}
