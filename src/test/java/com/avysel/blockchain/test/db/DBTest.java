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
	
	private final static String TestHash = "hhhhh";
	private final static long TestDifficulty = 456;
	private final static long TestIndex = 5;
	private final static String TestMerkleRoot = "4545465dddd";
	private final static String TestPreviousHash = "54f56ds4f5qf";
	private final static long TestTimestamp = System.currentTimeMillis();
	private final static String TestDataData = "Test Data";
	private final static String TestDataHash = UUID.randomUUID().toString();
	
	@BeforeClass
	public static void init() {
		testBlock.setHash(DBTest.TestHash);
		testBlock.setDifficulty(DBTest.TestDifficulty);
		testBlock.setIndex(DBTest.TestIndex);
		testBlock.setMerkleRoot(DBTest.TestMerkleRoot);
		testBlock.setPreviousHash(DBTest.TestPreviousHash);
		testBlock.setTimestamp(DBTest.TestTimestamp);
		
		testData.setData(DBTest.TestDataData);
		testData.setHash(DBTest.TestDataHash);
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
