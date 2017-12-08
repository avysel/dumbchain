package com.avysel.blockchain.network.server;

import com.avysel.blockchain.network.NetworkManager;

public class PeerListener implements Runnable {

	private NetworkManager manager;
	
	public PeerListener(NetworkManager manager) {
		super();
		this.manager = manager;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
