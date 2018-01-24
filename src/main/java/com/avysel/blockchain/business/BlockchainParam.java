package com.avysel.blockchain.business;

public class BlockchainParam {
	
	private boolean miningNode;
	private boolean canStartAlone;
	private boolean useNetwork;
	
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
