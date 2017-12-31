package com.avysel.blockchain;

import java.util.UUID;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.data.SingleData;

public class Main {

	private static Logger log = Logger.getLogger("com.avysel.blockchain.Main");

	public static void main (String[] args) {

		//setLogParameters();

		Blockchain blockchain = new Blockchain();
		processParams(args, blockchain);
		//log.info("Welcome to blockchain "+blockchain.toString());

		/*	initTestData(blockchain); */

		blockchain.start();
		blockchain.display();			

	}

	private static void setLogParameters() {
		Logger logRoot = Logger.getRootLogger();
		ConsoleAppender ca = new ConsoleAppender();
		PatternLayout pattern = new PatternLayout();
		pattern.setConversionPattern(UUID.randomUUID().toString().substring(0, 6)+ ":%d{ISO8601} - %-5p [%c] - %m%n");
		ca.setName("console");
		ca.setLayout(pattern);
		ca.activateOptions();
		logRoot.addAppender(ca);
		logRoot.setLevel(Level.INFO);		
	}

	private static void processParams(String[] args, Blockchain blockchain) {

		for(int i = 0 ; i < args.length ; i ++) {
			switch(args[i]) {
			case "-mining":
				String miningValue = args[++i];
				if("1".equals(miningValue)) {
					log.info("Start mining node");
					blockchain.setMining(Blockchain.MINING);
				}
				else if ("0".equals(miningValue)) {
					log.info("Start NOT mining node");
					blockchain.setMining(Blockchain.NOT_MINING);
				}
				else {
					log.error("Unknown value ' "+args[i]+"' for parameter -mining");
				}
				break;
			case "-help" :
				log.info("Help");
				break;
			default:
				log.error("Unknown parameter : "+args[i]);
				break;
			}
		}
	}

	private static void initTestData(Blockchain blockchain) {
		for(int i = 1 ; i < 100 ; i++) {
			try {
				blockchain.addIncomingData(new SingleData("data"+i));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
}
