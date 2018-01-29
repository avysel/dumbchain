package com.avysel.blockchain.demo;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.data.SingleData;

public class RandomDataGenerator implements Runnable{

	private boolean isRunning;
	private Blockchain blockchain;

	private static final int TIME_BETWEEN_TWO_DATA = 5000;
	
	public RandomDataGenerator(Blockchain blockchain) {
		this.blockchain = blockchain;
	}

	public void start() {
		this.isRunning = true;
		Thread t = new Thread(this);
		t.start();
		// TODO how to make it run ?
		
	}
	
	public void stop() {
		this.isRunning = false;
	}
	
	@Override
	public void run() {
		while(isRunning) {
			
			SingleData data = new SingleData();
			data.setData("data"+System.currentTimeMillis());
			
			try {
				blockchain.addData(data);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			wait(TIME_BETWEEN_TWO_DATA);
		}
	}
	
	private void wait(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
