package com.avysel.blockchain.test;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.demo.RandomDataGenerator;

public class RandomDataGeneratorTest {

	@Test
	public void testRandomDataGenerator() {
		Blockchain blockchain = new Blockchain();
		RandomDataGenerator rdg = new RandomDataGenerator(blockchain);

		int size = blockchain.getDataPool().size();

		rdg.start();

		assertNotEquals(size, blockchain.getDataPool().size());
	}
}
