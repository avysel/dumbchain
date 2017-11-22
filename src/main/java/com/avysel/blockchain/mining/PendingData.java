package com.avysel.blockchain.mining;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.avysel.blockchain.model.data.SingleData;

/**
 * Used to store the list of pending data. This class provides some operation on it, such as add data, pick random data ...
 */
public class PendingData {
	private Deque<SingleData> queue;

	public PendingData() {
		queue = new ArrayDeque<SingleData>();
	}
	
	private Deque<SingleData> getPendingData() {
		return queue;
	}

	
	/**
	 * Add a new @SingleData to the pending data list to be added in a @Block
	 * @param data the @SingleData to be added
	 */
	public void addData(SingleData data) {
		getPendingData().addLast(data);
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
			result.add(getPendingData().pop());
		}
		return result;
	}
	
	/**
	 * Pick a random quantity of data in the list of pending data.
	 * @return a @List<SingleData> that contains a random quantity of data
	 */
	public  List<SingleData> getRandomData() {
	//	System.out.println("Remains " + getPendingData().size()+" data");
		int quantity = (new Random()).nextInt(getPendingData().size() / 2) +1;
		List<SingleData> result = new ArrayList<SingleData>();
	//	System.out.println("Pick " + quantity +" data");
		for(int i=0 ; i < quantity ; i++) {
			result.add(getPendingData().pop());
		}
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
	
}