package com.avysel.blockchain.test;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.avysel.blockchain.business.Block;
import com.avysel.blockchain.crypto.HashTools;


public class HashToolsTest {

	private final String STRING_TO_HASH = "the string to hash";
	private final String RESULT_HASH = "f680674900b14bb6208817d78efc04ad78f4749695fad085ad366b4b763cb804";
	
	@Test
	public void testHash() {
		String hash = HashTools.calculateHash(STRING_TO_HASH);	

		assertEquals(hash, RESULT_HASH);
	}
	
}
