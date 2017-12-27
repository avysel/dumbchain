package com.avysel.blockchain.tools;

import com.avysel.blockchain.model.block.Block;

public class Util {
	public static byte[] bytes(String string) {
		return string.getBytes();
	}
	
	public static byte[] bytes(Block block) {
		return JsonMapper.blockToJson(block).getBytes();
	}
	
	public static String string(byte[] bytes) {
		return new String(bytes);
	}
	
}
