package com.avysel.blockchain.business;

public class BlockchainParameters {
	
	public static final boolean MINING_YES = true;
	public static final boolean MINING_NO = false;	
	
	public static final boolean CAN_START_ALONE_YES = true;
	public static final boolean CAN_START_ALONE_NO = false;	

	public static final boolean USE_NETWORK_YES = true;
	public static final boolean USE_NETWORK_NO = false;		
	
	private boolean miningNode;
	private boolean canStartAlone;
	private boolean useNetwork;
	
	public BlockchainParameters() {
		// init default
		miningNode = MINING_YES;
		canStartAlone = CAN_START_ALONE_YES;
		useNetwork = USE_NETWORK_YES;
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

}
