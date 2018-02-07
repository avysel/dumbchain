package com.avysel.blockchain;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.business.BlockchainParameters;

public final class Main {

	private static Logger log = Logger.getLogger(Main.class);
	
	private Main() {}
	
	/**
	 * Starts the blockchain.
	 * @param args parameters.
	 */
	public static void main(String[] args) {

		Blockchain blockchain = new Blockchain();
		
		log.info(BlockchainParameters.getUsage());
		
		BlockchainParameters params = processParams(args);
		blockchain.setParams(params);
		
		log.info("Welcome to blockchain "+blockchain.toString());

		blockchain.start();
		blockchain.display();			
	}

	public static final String SEPARATOR = "=";
	public static final String ENABLED = "1";
	public static final String DISABLED = "0";	
	
	private static BlockchainParameters processParams(String[] args) {

		BlockchainParameters params = new BlockchainParameters();
		
		for(int i=0; i<args.length; i++) {
			
			String param = args[i];
			String[] data = param.split(SEPARATOR);
			
			switch(data[0]) {
			case "-mining":
				String miningValue = data[1];
				if(ENABLED.equals(miningValue)) {
					log.info("Start mining node");
					params.setMiningNode(BlockchainParameters.Constants.MINING_YES);
				} else if (DISABLED.equals(miningValue)) {
					log.info("Start NOT mining node");
					params.setMiningNode(BlockchainParameters.Constants.MINING_NO);
				} else {
					log.error("Unknown value ' "+args[i]+"' for parameter -mining");
				}
				break;
			case "-demoDataGenerator":
				String dataGeneratorValue = data[1];
				if(ENABLED.equals(dataGeneratorValue)) {
					log.info("Start demo data generator node");
					params.setDemoDataGenerator(BlockchainParameters.Constants.DATA_GENERATOR_YES);
				} else if (DISABLED.equals(dataGeneratorValue)) {
					log.info("Start NOT demo data generator node");
					params.setDemoDataGenerator(BlockchainParameters.Constants.DATA_GENERATOR_NO);
				} else {
					log.error("Unknown value ' "+args[i]+"' for parameter -demoDataGenerator");
				}
				break;			
			case "-help" :
				System.out.println(BlockchainParameters.getUsage());
				break;
			default:
				log.error("Unknown parameter : "+args[i]);
				break;
			}
		}
		
		return params;
	}

/*	private static void initTestData(Blockchain blockchain) {
		for(int i=1;i < 100;i++) {
			try {
				blockchain.addIncomingData(new SingleData("data"+i));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}*/
	
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
}
