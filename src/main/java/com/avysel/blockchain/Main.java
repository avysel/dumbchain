package com.avysel.blockchain;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.business.BlockchainParameters;
import com.avysel.blockchain.model.data.SingleData;

public class Main {

	private static Logger log = Logger.getLogger(Main.class);

	public static void main (String[] args) {

		//setLogParameters();

		Blockchain blockchain = new Blockchain();
		blockchain.setParams(processParams(args));
		log.info("Welcome to blockchain "+blockchain.toString());

		initTestData(blockchain);

		blockchain.start();
		blockchain.display();			

	}

/*	private static void setLogParameters() {
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
*/
	private static BlockchainParameters processParams(String[] args) {

		BlockchainParameters params = new BlockchainParameters();
		
		for(int i = 0 ; i < args.length ; i ++) {
			switch(args[i]) {
			case "-mining":
				String miningValue = args[++i];
				if("1".equals(miningValue)) {
					log.info("Start mining node");
					params.setMiningNode(BlockchainParameters.MINING_YES);
				}
				else if ("0".equals(miningValue)) {
					log.info("Start NOT mining node");
					params.setMiningNode(BlockchainParameters.MINING_NO);
				}
				else {
					log.error("Unknown value ' "+args[i]+"' for parameter -mining");
				}
				break;
			case "-demoDataGenerator":
				String dataGeneratorValue = args[++i];
				if("1".equals(dataGeneratorValue)) {
					log.info("Start demo data generator node");
					params.setDemoDataGenerator(BlockchainParameters.DATA_GENERATOR_YES);
				}
				else if ("0".equals(dataGeneratorValue)) {
					log.info("Start NOT demo data generator node");
					params.setDemoDataGenerator(BlockchainParameters.DATA_GENERATOR_NO);
				}
				else {
					log.error("Unknown value ' "+args[i]+"' for parameter -demoDataGenerator");
				}
				break;
			case "-canStartAlone":
				String canStartAlone = args[++i];
				if("1".equals(canStartAlone)) {
					log.info("Start node that can start alone.");
					params.setCanStartAlone(BlockchainParameters.CAN_START_ALONE_YES);
				}
				else if ("0".equals(canStartAlone)) {
					log.info("Start node that can't sart withour other peers.");
					params.setCanStartAlone(BlockchainParameters.CAN_START_ALONE_NO);
				}
				else {
					log.error("Unknown value ' "+args[i]+"' for parameter -canStartAlone");
				}
				break;
			case "-help" :
				StringBuffer help = new StringBuffer();
				help.append("Usage : ");
				help.append("\n\n\t -mining 1 for a mining node. \n\t -mining 0 for a not mining node.");
				help.append("\n\n\t -demoDataGenerator 1 for a demo data generator node. \n\t -demoDataGenerator 0 for a not demo data generator node.");
				help.append("\n\n\t -canStartAlone 1 for a node that can start alone. \n\t -canStartAlone 0 for a node that cannot start without other peer.");
				System.out.println(help.toString());
				break;
			default:
				log.error("Unknown parameter : "+args[i]);
				break;
			}
		}
		
		return params;
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
