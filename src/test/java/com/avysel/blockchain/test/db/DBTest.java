package com.avysel.blockchain.test.db;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.business.block.Block;
import com.avysel.blockchain.business.data.SingleData;

public class DBTest {

	private static Block testBlock = new Block();
	private static SingleData testData = new SingleData("");
	
	private static final String TEST_HASH = "hhhhh";
	private static final long TEST_DIFFICULTY = 456;
	private static final long TEST_INDEX = 5;
	private static final String TEST_MERKLE_ROOT = "4545465dddd";
	private static final String TEST_PREVIOUS_HASH = "54f56ds4f5qf";
	private static final long TEST_TIMESTAMP = System.currentTimeMillis();
	private static final String TEST_DATA_DATA = "Test Data";
	private static final String TEST_DATA_HASH = UUID.randomUUID().toString();
	
	private static Blockchain blockchain;
	
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
		
		blockchain = new Blockchain();
		blockchain.addBlock(testBlock);
	}
	
/*	@Test
	public void writeBlockTest() {
		DBManager db = new DBManager();
		db.putBlock(testBlock);
		
		Block block = db.getBlock(testBlock.getHash());
		
		assertEquals(block, testBlock);
	}
*/	
	@Test
	public void testSaveLoadChain() {
		
		String nodeId = blockchain.getNodeId();
		long size = blockchain.getChain().size();
		String hash = blockchain.getChain().getLastBlock().getHash();
		
		blockchain.save();
		
		
		blockchain = new Blockchain();
		
		blockchain.load();
		
		assertEquals(nodeId, blockchain.getNodeId());
		assertEquals(size, blockchain.getChain().size());
		assertEquals(hash, blockchain.getChain().getLastBlock().getHash());
	}
}
