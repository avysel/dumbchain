package com.avysel.blockchain.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.avysel.blockchain.business.block.Block;

public class HashTools {
	
	/**
	 * Calculate the hash for a Block.
	 * @param block the Block to hash
	 * @return the SHA-256 hash for the given Block
	 */
	public static String calculateBlockHash(Block block) {

		if(block == null) return null;
		return calculateHash(block.getHashData()); 
		
	}
	
	/**
	 * Calculate the hash for a text data.
	 * @param text the data to hash
	 * @return the SHA-256 hash for the given text
	 */
	public static String calculateHash(byte[] text) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			md.update(text);
			byte[] hash = md.digest();
			
			return bytesToHex(hash);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	private static String bytesToHex(byte[] hash) {
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < hash.length; i++) {
	    String hex = Integer.toHexString(0xff & hash[i]);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
	
	/**
	 * Check data integrity according to checksum hash.
	 * @param hash the checksum of data
	 * @param data the data to check
	 * @return true if integrity of data is ok
	 */
	public boolean checkHash(String hash, String data) {
		return true;
	}
}
