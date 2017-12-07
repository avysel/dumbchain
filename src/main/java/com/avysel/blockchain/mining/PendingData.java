package com.avysel.blockchain.mining;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import com.avysel.blockchain.model.data.ISingleData;

/**
 * Used to store the list of pending data. This class provides some operation on it, such as add data, pick random data ...
 * It uses a synchronized queue, fed by the network and consumed by the Miner.
 */
public class PendingData {
	private LinkedBlockingQueue<ISingleData> queue;

	public PendingData() {
		queue = new LinkedBlockingQueue<ISingleData>();
	}

	private LinkedBlockingQueue<ISingleData> getPendingData() {
		return queue;
	}


	/**
	 * Add a new @SingleData to the pending data list to be added in a @Block
	 * @param data the @SingleData to be added
	 * @throws InterruptedException 
	 */
	public void addData(ISingleData data) throws InterruptedException {
		getPendingData().put(data);
	}

	/**
	 * Add a @List of @SingleData to the pending data list to be added in a @Block
	 * @param dataList the @List of @SingleData to be added
	 */	
	public void addAll(List<ISingleData> dataList) {
		getPendingData().addAll(dataList);
	}

	public List<ISingleData> getData(long quantity) {
		List<ISingleData> result = new ArrayList<ISingleData>();
		for(int i=0 ; i < quantity && !getPendingData().isEmpty() ; i++) {
			result.add(getPendingData().poll());
		}
		return result;
	}

	/**
	 * Pick a random quantity of data in the list of pending data.
	 * @return a @List<SingleData> that contains a random quantity of data
	 */
	public  List<ISingleData> getRandomData() {
		int quantity;
		List<ISingleData> result = new ArrayList<ISingleData>();
		do {
			// random quatity of data to take in pending data (no more than half of remaining)
			quantity = (new Random()).nextInt(getPendingData().size() / 2) +1;

			// if enough data, take it
			if(quantity <= getPendingData().size()) {
				for(int i=0 ; i < quantity ; i++) {
					result.add(getPendingData().poll());
				}
			}
			
			// if not enough data, try again (wait for a suitable quantity, or for the queue to be filled)
		} while(quantity > getPendingData().size());

		return result;
	}

	/**
	 * Returns the number of remaining pending data
	 * @return the size of pending data list
	 */
	public int size() {
		return getPendingData().size();
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		Iterator<ISingleData> it = getPendingData().iterator();
		while(it.hasNext())
			result.append(it.next());
		return result.toString();
	}

	/**
	 * Allows to know if a given data is already present in the pending data.
	 * @param uniqueId unique identifier of data to check if exists
	 * @return true if a data with the same unique identifier is present in queue
	 */
	public boolean exists(String uniqueId) {
		Iterator<ISingleData> it = getPendingData().iterator();
		while(it.hasNext())
			if(it.next().getUniqueId().equals(uniqueId))
				return true;
		return false;
	}

}