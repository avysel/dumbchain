package com.avysel.blockchain;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.data.SingleData;

public class Main {
	public static void main (String[] args) {
		
		Blockchain manager = new Blockchain();

		for(int i = 1 ; i < 100 ; i++)
			try {
				manager.addIncomingData(new SingleData("data"+i));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	
		manager.start();
		manager.display();				
	}
}
