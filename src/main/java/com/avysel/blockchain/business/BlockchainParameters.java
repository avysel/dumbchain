package com.avysel.blockchain.business;

public class BlockchainParameters {
	
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
