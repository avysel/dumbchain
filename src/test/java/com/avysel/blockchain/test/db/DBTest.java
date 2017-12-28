package com.avysel.blockchain.test.db;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;

import com.avysel.blockchain.db.DBManager;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.SingleData;

public class DBTest {

	private static Block testBlock = new Block();
	private static SingleData testData = new SingleData();
	
	private final static String testHash = "hhhhh";
	private final static long testDifficulty = 456;
	private final static long testIndex = 5;
	private final static String testMerkleRoot = "4545465dddd";
	private final static String testPreviousHash = "54f56ds4f5qf";
	private final static long testTimestamp = System.currentTimeMillis();
	private final static String testDataData = "Test Data";
	private final static String testUid = UUID.randomUUID().toString();
	
	@BeforeClass
	public static void init() {
		testBlock.setHash(DBTest.testHash);
		testBlock.setDifficulty(DBTest.testDifficulty);
		testBlock.setIndex(DBTest.testIndex);
		testBlock.setMerkleRoot(DBTest.testMerkleRoot);
		testBlock.setPreviousHash(DBTest.testPreviousHash);
		testBlock.setTimestamp(DBTest.testTimestamp);
		
		testData.setData(DBTest.testDataData);
		testData.setUniqueId(DBTest.testUid);
	}
	
	@Test
	public void writeBlockTest() {
		DBManager db = new DBManager();
		db.putBlock(testBlock);
	}
	
	@Test
	public void readBlockTest() {
		
	}
	
	@Test
	public void writeDataTest() {
		DBManager db = new DBManager();
		db.putData(testData);		
	}
	
	@Test
	public void readDataTest() {
		
	}
	
}
