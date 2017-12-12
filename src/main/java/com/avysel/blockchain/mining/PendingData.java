package com.avysel.blockchain.mining;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.avysel.blockchain.model.data.SingleData;

/**
 * Used to store the list of pending data. This class provides some operation on it, such as add data, pick random data ...
 * It uses a synchronized queue, fed by the network and consumed by the Miner.
 */
public class PendingData {
	
	private static Logger log = Logger.getLogger("com.avysel.blockchain.mining.PendingData");
	
	private LinkedBlockingQueue<SingleData> queue;

	public PendingData() {
		queue = new LinkedBlockingQueue<SingleData>();
	}

	private LinkedBlockingQueue<SingleData> getPendingData() {
		return queue;
	}

	/**
	 * Add a new @SingleData to the pending data list to be added in a @Block
	 * @param data the @SingleData to be added
	 * @throws InterruptedException 
	 */
	public void addData(SingleData data) throws InterruptedException {
		
		if(!exists(data.getUniqueId())) {
			getPendingData().put(data);
			log.info("New data in pool");
			log.debug(data);
		}
		else {
			log.warn("Data "+data.getUniqueId()+" already exists");
		}
	}

	/**
	 * Add a @List of @SingleData to the pending data list to be added in a @Block
	 * @param dataList the @List of @SingleData to be added
	 */	
	public void addAll(List<SingleData> dataList) {
		getPendingData().addAll(dataList);
	}

	public List<SingleData> getData(long quantity) {
		List<SingleData> result = new ArrayList<SingleData>();
		for(int i=0 ; i < quantity && !getPendingData().isEmpty() ; i++) {
			result.add(getPendingData().poll());
		}
		return result;
	}

	/**
	 * Pick a random quantity of data in the list of pending data.
	 * @return a @List<SingleData> that contains a random quantity of data
	 */
	public  List<SingleData> getRandomData() { // TODO how to use the "blocking" feature of this queue when reading ?
		int quantity;
		List<SingleData> result = new ArrayList<SingleData>();
		do {
			// random quantity of data to take in pending data (no more than half of remaining)
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
		Iterator<SingleData> it = getPendingData().iterator();
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
		Iterator<SingleData> it = getPendingData().iterator();
		while(it.hasNext())
			if(it.next().getUniqueId().equals(uniqueId))
				return true;
		return false;
	}

}