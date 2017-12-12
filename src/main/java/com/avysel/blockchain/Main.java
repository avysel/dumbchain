package com.avysel.blockchain;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.data.SingleData;

public class Main {
	
	private static Logger log = Logger.getLogger("com.avysel.blockchain.Main");
	
	public static void main (String[] args) {
		
	    Logger logRoot = Logger.getRootLogger();
	    ConsoleAppender ca = new ConsoleAppender();
	    PatternLayout pattern = new PatternLayout();
	    pattern.setConversionPattern("%d{ISO8601} - %-5p [%c] - %m%n");
	    ca.setName("console");
	    ca.setLayout(pattern);
	    ca.activateOptions();
	    logRoot.addAppender(ca);
	    logRoot.setLevel(Level.INFO);
	    
		Blockchain manager = new Blockchain();

		for(int i = 1 ; i < 100 ; i++) {
			try {
				manager.addIncomingData(new SingleData("data"+i));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		manager.start();
		manager.display();				
	}
}
