package com.avysel.blockchain.network.client;

import com.avysel.blockchain.network.NetworkManager;

public class PeerExplorer implements Runnable {

	private NetworkManager manager;
	private boolean running = true;

	public PeerExplorer(NetworkManager manager) {
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

	private void explore() {
		
	}
	
}
