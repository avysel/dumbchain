package com.avysel.blockchain;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {

	Properties properties = new Properties();

	private int minDataInBlock;
	private int maxDataInBlock;
	private int maxBlocksInBulk;
	private long unsafeHeight;
	private String proofOfWorkPrefix;
	private int fallbackProofOfWorkStep;
	private String fallbackProofOfWorkPrefix;
	private int waitForPeersTime;
	private int waitForCatchupTime;
	private String dbPath;
	private int peerListeningPort;

	public static final class Keys {
		public static final String min_data_in_block="min_data_in_block";
		public static final String	max_data_in_block="max_data_in_block";
		public static final String	max_blocks_in_bulk="max_blocks_in_bulk";
		public static final String unsafe_height="unsafe_height";
		public static final String proof_of_work_prefix="proof_of_work_prefix";
		public static final String fallback_proof_of_work_step="fallback_proof_of_work_step";
		public static final String fallback_proof_of_work_prefix="fallback_proof_of_work_prefix";
		public static final String wait_for_peers_time="wait_for_peers_time";
		public static final String wait_for_catchup_time="wait_for_catchup_time";
		public static final String db_path="db_path";
		public static final String peer_listening_port="network.peer_listening_port";
	}

	public PropertiesManager() {
		load(null);
	}

	public PropertiesManager(String fileName) {
		load(fileName);
	}	
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public void setProperty(String key, String value) {

	}

	public void load(String fileName) {

		InputStream input = null;

		try {
			if(fileName == null)
				fileName = "config.properties"; 
			input = Main.class.getClassLoader().getResourceAsStream(fileName);
			if(input==null){
				System.out.println("Sorry, unable to find " + fileName);
				return;
			}

			//load a properties file from class path, inside static method
			properties.load(input);
		
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
		
		initValues();
	}

	private void initValues() {
		this.minDataInBlock = Integer.parseInt(getProperty(Keys.min_data_in_block));
		this.maxDataInBlock = Integer.parseInt(getProperty(Keys.max_data_in_block));
		this.maxBlocksInBulk = Integer.parseInt(getProperty(Keys.max_blocks_in_bulk));
		this.unsafeHeight = Long.parseLong(getProperty(Keys.unsafe_height));
		this.proofOfWorkPrefix = getProperty(Keys.proof_of_work_prefix);
		this.fallbackProofOfWorkStep = Integer.parseInt(getProperty(Keys.fallback_proof_of_work_step));
		this.fallbackProofOfWorkPrefix = getProperty(Keys.fallback_proof_of_work_prefix);
		this.waitForPeersTime = Integer.parseInt(getProperty(Keys.wait_for_peers_time));
		this.waitForCatchupTime = Integer.parseInt(getProperty(Keys.wait_for_catchup_time));
		this.dbPath	= getProperty(Keys.db_path);
		this.peerListeningPort = Integer.parseInt(getProperty(Keys.peer_listening_port));
	}
	
	public void writeProperties() {

	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * Minimum number of data to include in a block.
	 * @return Minimum number of data to include in a block.
	 */	
	public int getMinDataInBlock() {
		return minDataInBlock;
	}

	public void setMinDataInBlock(int minDataInBlock) {
		this.minDataInBlock = minDataInBlock;
	}

	/**
	 * Maximum number of data to include in a block.
	 * @return Maximum number of data to include in a block.
	 */		
	public int getMaxDataInBlock() {
		return maxDataInBlock;
	}

	public void setMaxDataInBlock(int maxDataInBlock) {
		this.maxDataInBlock = maxDataInBlock;
	}

	/**
	 * Maximum number of block that can be sent in a message when sent blocks for catch-up.
	 * @return Maximum number of block that can be sent in a message when sent blocks for catch-up.
	 */		
	public int getMaxBlocksInBulk() {
		return maxBlocksInBulk;
	}

	public void setMaxBlocksInBulk(int maxBlocksInBulk) {
		this.maxBlocksInBulk = maxBlocksInBulk;
	}

	/**
	 * Number of last blocks that are considered as unsafe because they still can be unlinked 
	 * and replaced by other blocks due to consensus.
	 * @return Number of last blocks that are considered as unsafe because they still can be unlinked 
	 * and replaced by other blocks due to consensus.
	 */
	public long getUnsafeHeight() {
		return unsafeHeight;
	}

	public void setUnsafeHeight(long unsafeHeight) {
		this.unsafeHeight = unsafeHeight;
	}

	public String getProofOfWorkPrefix() {
		return proofOfWorkPrefix;
	}

	public void setProofOfworkPrefix(String proofOfworkPrefix) {
		this.proofOfWorkPrefix = proofOfworkPrefix;
	}

	public int getFallbackProofOfWorkStep() {
		return fallbackProofOfWorkStep;
	}

	public void setFallbackProofOfWorkStep(int fallbackProofOfWorkStep) {
		this.fallbackProofOfWorkStep = fallbackProofOfWorkStep;
	}

	public String getFallbackProofOfWorkPrefix() {
		return fallbackProofOfWorkPrefix;
	}

	public void setFallbackProofOfWorkPrefix(String fallbackProofOfWorkPrefix) {
		this.fallbackProofOfWorkPrefix = fallbackProofOfWorkPrefix;
	}

	public int getWaitForPeersTime() {
		return waitForPeersTime;
	}

	public void setWaitForPeersTime(int waitForPeersTime) {
		this.waitForPeersTime = waitForPeersTime;
	}

	public int getWaitForCatchupTime() {
		return waitForCatchupTime;
	}

	public void setWaitForCatchupTime(int waitForCatchupTime) {
		this.waitForCatchupTime = waitForCatchupTime;
	}

	public String getDbPath() {
		return dbPath;
	}

	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}

	public int getPeerListeningPort() {
		return peerListeningPort;
	}

	public void setPeerListeningPort(int peerListeningPort) {
		this.peerListeningPort = peerListeningPort;
	}

	public void setProofOfWorkPrefix(String proofOfWorkPrefix) {
		this.proofOfWorkPrefix = proofOfWorkPrefix;
	}
}
