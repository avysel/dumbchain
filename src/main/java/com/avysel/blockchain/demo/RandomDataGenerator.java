package com.avysel.blockchain.demo;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.data.SingleData;

/**
 * A simulator to generate random data and send it to the network.
 */
public class RandomDataGenerator implements Runnable {

	private static Logger log = Logger.getLogger(RandomDataGenerator.class);
	
	private boolean isRunning;
	private Blockchain blockchain;

	private static final int TIME_BETWEEN_TWO_DATA = 5000;
	
	public RandomDataGenerator(Blockchain blockchain) {
		this.blockchain = blockchain;
	}

	/**
	 * Start random data generation
	 */
	public void start() {
		log.info("Start Random data generator");
		this.isRunning = true;
		Thread t = new Thread(this);
		t.start();
	}
	
	/**
	 * Stop random data generation
	 */
	public void stop() {
		log.info("Stop Random data generator");
		this.isRunning = false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(isRunning) {
			
			SingleData data = new SingleData();
			data.setData("data"+System.currentTimeMillis());
			log.debug("New random data created : "+data);
			try {
				blockchain.addData(data);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			wait(TIME_BETWEEN_TWO_DATA);
		}
	}
	
	/**
	 * Pause current thread
	 * @param time pause duration in milliseconds
	 */
	private void wait(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
