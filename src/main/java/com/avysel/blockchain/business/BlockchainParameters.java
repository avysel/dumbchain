package com.avysel.blockchain.business;

import java.nio.charset.Charset;

/** 
 * Parameters for blockchain.
 */
public class BlockchainParameters {
	
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	
	/**
	 * Maximum number of data to include in a block.
	 */
	public static final int MAX_DATA_IN_BLOCK = 500;	
	
	/**
	 * Maximum number of block that can be sent in a message when sent blocks for catch-up.
	 */
	public static final int MAX_BLOCKS_PER_BULK = 5;	
	
	public static final String SEPARATOR = "=";
	public static final String ENABLED = "1";
	public static final String DISABLED = "0";
	
	public static final boolean MINING_YES = true;
	public static final boolean MINING_NO = false;	
	
	public static final boolean CAN_START_ALONE_YES = true;
	public static final boolean CAN_START_ALONE_NO = false;	

	public static final boolean USE_NETWORK_YES = true;
	public static final boolean USE_NETWORK_NO = false;		

	public static final boolean DATA_GENERATOR_YES = true;
	public static final boolean DATA_GENERATOR_NO = false;		
	
	private boolean miningNode;
	private boolean canStartAlone;
	private boolean useNetwork;
	private boolean demoDataGenerator;
	
	public BlockchainParameters() {
		// init default
		miningNode = MINING_YES;
		canStartAlone = CAN_START_ALONE_YES;
		useNetwork = USE_NETWORK_YES;
		demoDataGenerator = DATA_GENERATOR_YES;
	}
	
	public boolean isMiningNode() {
		return miningNode;
	}
	public void setMiningNode(boolean miningNode) {
		this.miningNode = miningNode;
	}
	public boolean isCanStartAlone() {
		return canStartAlone;
	}
	public void setCanStartAlone(boolean canStartAlone) {
		this.canStartAlone = canStartAlone;
	}
	public boolean isUseNetwork() {
		return useNetwork;
	}
	public void setUseNetwork(boolean useNetwork) {
		this.useNetwork = useNetwork;
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
		help.append("\n\n\t -canStartAlone=1 for a node that can start alone (default).")
		.append("\n\t -canStartAlone=0 for a node that cannot start without other peer.");
		help.append("\n\n\t -useNetwork=1 for a node that listen to network to catch new data (default).")
		.append("\n\t -useNetwork=0 for a node that doesn't listen to network to catch new data. (mining is therefore disabled)");
		
		help.append("\n\n\tDemo parameters : ");
		help.append("\n\n\t -demoDataGenerator=1 for a demo data generator node.")
		.append("\n\t -demoDataGenerator=0 for a no demo data generator node. (default)");
		return help.toString();
	}
}
