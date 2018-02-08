package com.avysel.blockchain.tools;

import com.avysel.blockchain.business.BlockchainParameters;
import com.avysel.blockchain.business.block.Block;
import com.avysel.blockchain.business.chain.Chain;

/**
 * Provides some useful methods.
 */
public final class Util {

	private Util() {}

	/**
	 * Get a bytes array from a string.
	 * @param string the string.
	 * @return the bytes array.
	 */
	public static byte[] bytes(String string) {
		if(string == null)
			return null;
		return string.getBytes(BlockchainParameters.DEFAULT_CHARSET);
	}

	/**
	 * Get a bytes array from a chain.
	 * @param chain the chain.
	 * @return the bytes array.
	 */
	public static byte[] bytes(Chain chain) {
		return JsonMapper.chainToJson(chain).getBytes(BlockchainParameters.DEFAULT_CHARSET);
	}	

	/**
	 * Get a bytes array from a block.
	 * @param block the block.
	 * @return the bytes array.
	 */
	public static byte[] bytes(Block block) {
		return JsonMapper.blockToJson(block).getBytes(BlockchainParameters.DEFAULT_CHARSET);
	}

	/**
	 * Get a string from a bytes array.
	 * @param bytes the bytes array.
	 * @return the string.
	 */
	public static String string(byte[] bytes) {
		if(bytes != null)
			return new String(bytes, BlockchainParameters.DEFAULT_CHARSET);
		else 
			return null;
	}

}
