package com.avysel.blockchain.business.data;

import java.util.List;

import com.avysel.blockchain.mining.DataPool;
import com.avysel.blockchain.network.NetworkManager;

/**
 * Check if data stay too much time in data pool, and send them again to the network.
 */
public class DataSender implements Runnable {

	private DataPool dataPool;
	private NetworkManager network;	
	private Long lastBlockTimestamp;

	private boolean running;

	public DataSender(DataPool dataPool, NetworkManager network) {
		super();
		this.dataPool = dataPool;
		this.network = network;
	}

	public Long getLastBlockTimestamp() {
		return lastBlockTimestamp;
	}
	public void setLastBlockTimestamp(Long lastBlockTimestamp) {
		this.lastBlockTimestamp = lastBlockTimestamp;
	}

	public void start() {
		running = true;
		Thread t = new Thread(this);
		t.start();
	}	

	public void stop() {
		running = false;
	}

	@Override
	public void run() {

		while(running) {
			try {
				Thread.sleep(1000 * 60 * 10); // sleep 10 minutes
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 

			if(lastBlockTimestamp != null) {
				
				// get data older than last block
				List<ISingleData> oldData = dataPool.getDataOlderThan(lastBlockTimestamp);

				// send again to network
				for(ISingleData data : oldData) {
					network.sendData(data);
				}
			}
		}
	}
}
