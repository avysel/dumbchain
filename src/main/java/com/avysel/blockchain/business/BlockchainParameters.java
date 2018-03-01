package com.avysel.blockchain.business;

import java.nio.charset.Charset;

import com.avysel.blockchain.PropertiesManager;

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
	
	
	private boolean miningNode;
	private boolean demoDataGenerator;
	public PropertiesManager properties;
	
	public BlockchainParameters() {
		miningNode = Constants.MINING_YES;
		demoDataGenerator = Constants.DATA_GENERATOR_NO;
		properties = new PropertiesManager("config.properties");
	}
	
	public BlockchainParameters(String fileName) {
		// init default
		miningNode = Constants.MINING_YES;
		demoDataGenerator = Constants.DATA_GENERATOR_NO;
		properties = new PropertiesManager(fileName);		
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
		help.append("\n\n\t -properties=fileName to specify a properties file");
		help.append("\n\n\t -mining=1 for a mining node (default).")
		.append("\n\t -mining=0 for a not mining node.");
		
		help.append("\n\n\tDemo parameters : ");
		help.append("\n\n\t -demoDataGenerator=1 for a demo data generator node.")
		.append("\n\t -demoDataGenerator=0 for a no demo data generator node. (default)");
		return help.toString();
	}

	public PropertiesManager getProperties() {
		return properties;
	}
	
	public void loadProperties(String fileName) {
		properties.load(fileName);
	}

	/**
	 * Minimum number of data to include in a block.
	 * @return Minimum number of data to include in a block.
	 */	
	public int getMinDataInBlock() {
		return properties.getMinDataInBlock();
	}

	/**
	 * Maximum number of data to include in a block.
	 * @return Maximum number of data to include in a block.
	 */		
	public int getMaxDataInBlock() {
		return properties.getMaxDataInBlock();
	}

	/**
	 * Maximum number of block that can be sent in a message when sent blocks for catch-up.
	 * @return Maximum number of block that can be sent in a message when sent blocks for catch-up.
	 */		
	public int getMaxBlocksInBulk() {
		return properties.getMaxBlocksInBulk();
	}

	/**
	 * Number of last blocks that are considered as unsafe because they still can be unlinked 
	 * and replaced by other blocks due to consensus.
	 * @return Number of last blocks that are considered as unsafe because they still can be unlinked 
	 * and replaced by other blocks due to consensus.
	 */
	public long getUnsafeHeight() {
		return properties.getUnsafeHeight();
	}

	public String getProofOfWorkPrefix() {
		return properties.getProofOfWorkPrefix();
	}

	public int getFallbackProofOfWorkStep() {
		return properties.getFallbackProofOfWorkStep();
	}

	public String getFallbackProofOfWorkPrefix() {
		return properties.getFallbackProofOfWorkPrefix();
	}

	public int getWaitForPeersTime() {
		return properties.getWaitForPeersTime();
	}

	public int getWaitForCatchupTime() {
		return properties.getWaitForCatchupTime();
	}

	public String getDbPath() {
		return properties.getDbPath();
	}

	public int getPeerListeningPort() {
		return properties.getPeerListeningPort();
	}	
}
