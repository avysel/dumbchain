package com.avysel.blockchain.test.db;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.avysel.blockchain.db.DBManager;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.SingleData;

public class DBTest {

	private static Block testBlock = new Block();
	private static SingleData testData = new SingleData("");
	
	private final static String TEST_HASH = "hhhhh";
	private final static long TEST_DIFFICULTY = 456;
	private final static long TEST_INDEX = 5;
	private final static String TEST_MERKLE_ROOT = "4545465dddd";
	private final static String TEST_PREVIOUS_HASH = "54f56ds4f5qf";
	private final static long TEST_TIMESTAMP = System.currentTimeMillis();
	private final static String TEST_DATA_DATA = "Test Data";
	private final static String TEST_DATA_HASH = UUID.randomUUID().toString();
	
	@BeforeClass
	public static void init() {
		testBlock.setHash(DBTest.TEST_HASH);
		testBlock.setDifficulty(DBTest.TEST_DIFFICULTY);
		testBlock.setIndex(DBTest.TEST_INDEX);
		testBlock.setMerkleRoot(DBTest.TEST_MERKLE_ROOT);
		testBlock.setPreviousHash(DBTest.TEST_PREVIOUS_HASH);
		testBlock.setTimestamp(DBTest.TEST_TIMESTAMP);
		
		testData.setData(DBTest.TEST_DATA_DATA);
		testData.setHash(DBTest.TEST_DATA_HASH);
		
		testBlock.addData(testData);
	}
	
	@Test
	public void writeBlockTest() {
		DBManager db = new DBManager();
		db.putBlock(testBlock);
		
		Block block = db.getBlock(testBlock.getHash());
		
		assertEquals(block, testBlock);
	}
	
	
/*	@Test
	public void writeDataTest() {
		DBManager db = new DBManager();
		db.putData(testData);		
	}
	*/
	
}
