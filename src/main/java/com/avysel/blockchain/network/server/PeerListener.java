package com.avysel.blockchain.network.server;

import com.avysel.blockchain.network.NetworkManager;

public class PeerListener implements Runnable {

	private NetworkManager manager;
	private boolean running = true;

	public PeerListener(NetworkManager manager) {
		super();
		this.manager = manager;
	}


	@Override
	public void run() {
		
		while (running) {
			
		}

	}
	
	public void stop() {
		running = false;
	}

}
