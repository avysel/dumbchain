package com.avysel.blockchain;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.data.SingleData;

public class Main {
	public static void main (String[] args) {
		
	    Logger logRoot = Logger.getRootLogger();
	    ConsoleAppender ca = new ConsoleAppender();
	    ca.setName("console");
	    ca.setLayout(new SimpleLayout());
	    ca.activateOptions();
	    logRoot.addAppender(ca);
	    logRoot.setLevel(Level.INFO);
		
	    Logger log = Logger.getLogger("com.avysel.blockchain.Main");
	    log.error("test");
	    
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
