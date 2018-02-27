package com.avysel.blockchain.mining;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.data.ISingleData;

/**
 * Used to store the list of pending data. This class provides some operation on it, such as add data, pick data ...
 * It uses a synchronized queue, fed by the network and consumed by the Miner.
 */
public class DataPool {

	private static Logger log = Logger.getLogger(DataPool.class);

	private LinkedBlockingQueue<ISingleData> queue;

	public DataPool() {
		queue = new LinkedBlockingQueue<ISingleData>();
	}

	private LinkedBlockingQueue<ISingleData> getDataPool() {
		return queue;
	}

	/**
	 * Add a new SingleData to the pending data list to be added in a Block.
	 * @param data the SingleData to be added
	 * @throws InterruptedException when a synchronization problem occurs
	 */
	public void addData(ISingleData data) throws InterruptedException {

		if(!exists(data.getHash())) {
			getDataPool().put(data);
			log.debug("New data in pool : "+data);
		} else {
			log.warn("Data "+data.getHash()+" already exists");
		}
	}

	/**
	 * Add a List of SingleData to the pending data list to be added in a Block.
	 * @param dataList the List of SingleData to be added
	 */	
	public void addAll(List<ISingleData> dataList) {
		getDataPool().addAll(dataList);
	}

	public void removeAll(List<ISingleData> dataList) {
		getDataPool().removeAll(dataList);
	}

	/**
	 * Pick a quantity of data in the list of pending data.
	 * @param quantity the number of data to pick. If pool size is lower than expected quantity, all remaining data will be picked up.
	 * @return a List of data
	 */
	public  List<ISingleData> pickData(int quantity) {
		List<ISingleData> result = new ArrayList<ISingleData>();

		if(getDataPool().isEmpty()) return result;

		// pick no more than pool size
		quantity = Math.min(getDataPool().size(), quantity);

		// if enough data, take it
		if(quantity <= getDataPool().size()) {
			for(int i=0; i<quantity; i++) {
				result.add(getDataPool().poll());
			}
		}

		return result;
	}

	/**
	 * Returns the number of remaining pending data.
	 * @return the size of pending data list
	 */
	public int size() {
		return getDataPool().size();
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		Iterator<ISingleData> it = getDataPool().iterator();
		while(it.hasNext())
			result.append(it.next());
		return result.toString();
	}

	/**
	 * Allows to know if a given data is already present in the pending data.
	 * @param hash hash of data to check if exists
	 * @return true if a data with the same unique identifier is present in queue
	 */
	public boolean exists(String hash) {
		Iterator<ISingleData> it = getDataPool().iterator();
		while(it.hasNext())
			if(it.next().getHash().equals(hash))
				return true;
		return false;
	}
	
	/**
	 * Get all data created before a giver time.
	 * @param timestamp the time.
	 * @return all data created before given timestamp.
	 */
	public List<ISingleData> getDataOlderThan(Long timestamp) {
		List<ISingleData> result = new ArrayList<ISingleData>();
		
		for(ISingleData data : getDataPool()) {
			if(data.getTimestamp() < timestamp) {
				result.add(data);
			}
		}
		
		return result;		
	}
}
