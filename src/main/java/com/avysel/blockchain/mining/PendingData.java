package com.avysel.blockchain.mining;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.avysel.blockchain.model.SingleData;

public class PendingData {
	private Deque<SingleData> queue;

	public PendingData() {
		queue = new ArrayDeque<SingleData>();
	}
	
	private Deque<SingleData> getPendingData() {
		return queue;
	}

	
	public void addData(SingleData data) {
		getPendingData().addLast(data);
	}
	
	public void addAll(List<SingleData> dataList) {
		// TODO add first pour les remettre a leur place ? 
		// a priori non, pour maintenir le picking aleatoire a chaque tentative de minage
		getPendingData().addAll(dataList);
	}
	
	public List<SingleData> getData(long quantity) {
		List<SingleData> result = new ArrayList<SingleData>();
		for(int i=0 ; i < quantity && !getPendingData().isEmpty() ; i++) {
			result.add(getPendingData().pop());
		}
		return result;
	}
	
	public  List<SingleData> getRandomData() {
		System.out.println("Remains " + getPendingData().size()+" data");
		int quantity = (new Random()).nextInt(getPendingData().size() / 2) +1;
		List<SingleData> result = new ArrayList<SingleData>();
		System.out.println("Pick " + quantity +" data");
		for(int i=0 ; i < quantity ; i++) {
			result.add(getPendingData().pop());
		}
		return result;
	}
	
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