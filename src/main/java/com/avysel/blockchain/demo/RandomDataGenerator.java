package com.avysel.blockchain.demo;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.business.data.custom.SingleData;

/**
 * A simulator to generate random data and send it to the network.
 */
public class RandomDataGenerator implements Runnable {

	private static Logger log = Logger.getLogger(RandomDataGenerator.class);
	
	/**
	 * Data generator is running ?
	 */
	private boolean isRunning;
	
	/**
	 * The blockchain to be fed wirt generated data. 
	 */
	private Blockchain blockchain;

	/**
	 * Sleeping time bewteen two data creation.
	 */
	private static final int TIME_BETWEEN_TWO_DATA = 500;
	
	public RandomDataGenerator(Blockchain blockchain) {
		this.blockchain = blockchain;
	}

	/**
	 * Start random data generation.
	 */
	public void start() {
		log.info("Start Random data generator");
		this.isRunning = true;
		Thread t = new Thread(this);
		t.start();
	}
	
	/**
	 * Stop random data generation.
	 */
	public void stop() {
		log.info("Stop Random data generator");
		this.isRunning = false;
	}
	
	@Override
	public void run() {
		while(isRunning) {
			
			SingleData data = new SingleData("data"+System.currentTimeMillis());

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
	 * Pause current thread.
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
