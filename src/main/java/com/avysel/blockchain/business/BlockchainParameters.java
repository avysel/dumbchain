package com.avysel.blockchain.business;

import java.nio.charset.Charset;

/** 
 * Parameters for blockchain.
 */
public class BlockchainParameters {
	
	public static class Constants {
		private Constants() {}
		
		public static final boolean MINING_YES = true;
		public static final boolean MINING_NO = false;	

		public static final boolean DATA_GENERATOR_YES = true;
		public static final boolean DATA_GENERATOR_NO = false;		
	}
	
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	/**
	 * Minimum number of data to include in a block.
	 */
	public static final int MIN_DATA_IN_BLOCK = 50;	
	
	/**
	 * Maximum number of data to include in a block.
	 */
	public static final int MAX_DATA_IN_BLOCK = 500;	
	
	/**
	 * Maximum number of block that can be sent in a message when sent blocks for catch-up.
	 */
	public static final int MAX_BLOCKS_PER_BULK = 5;	
	
	/**
	 * Number of last blocks that are considered as unsafe because they still can be unlinked 
	 * and replaced by other blocks due to consensus.
	 */
	public static final int UNSAFE_HEIGTH = 5;
	
	private boolean miningNode;
	private boolean demoDataGenerator;
	
	public BlockchainParameters() {
		// init default
		miningNode = Constants.MINING_YES;
		demoDataGenerator = Constants.DATA_GENERATOR_YES;
	}
	
	public boolean isMiningNode() {
		return miningNode;
	}
	public void setMiningNode(boolean miningNode) {
		this.miningNode = miningNode;
	}
	public boolean isDemoDataGenerator() {
		return demoDataGenerator;
	}
	public void setDemoDataGenerator(boolean demoDataGenerator) {
		this.demoDataGenerator = demoDataGenerator;
	}
	
	public static String getUsage() {
		StringBuffer help = new StringBuffer();
		help.append("Usage : ");
		help.append("\n\n\tRegular use parameters : ");
		help.append("\n\n\t -help displays this help menu.");
		help.append("\n\n\t -mining=1 for a mining node (default).")
		.append("\n\t -mining=0 for a not mining node.");
		
		help.append("\n\n\tDemo parameters : ");
		help.append("\n\n\t -demoDataGenerator=1 for a demo data generator node.")
		.append("\n\t -demoDataGenerator=0 for a no demo data generator node. (default)");
		return help.toString();
	}
}
