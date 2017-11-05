package com.avysel.blockchain.business;

public class ChainManager {
	private Chain chain;

	public ChainManager() {
		
	}
	
	public ChainManager(Chain chain) {
		this.chain = chain;
	}
	
	public Chain getChain() {
		return chain;
	}

	public void setChain(Chain chain) {
		this.chain = chain;
	}


}
