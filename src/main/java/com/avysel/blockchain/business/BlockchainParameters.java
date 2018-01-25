package com.avysel.blockchain.business;

public class BlockchainParameters {
	
	public static final boolean MINING = true;
	public static final boolean NOT_MINING = false;	
	
	private boolean miningNode;
	private boolean canStartAlone;
	private boolean useNetwork;
	
	public BlockchainParameters() {
		
		// init default
		miningNode = true;
		canStartAlone = true;
		useNetwork = true;
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
