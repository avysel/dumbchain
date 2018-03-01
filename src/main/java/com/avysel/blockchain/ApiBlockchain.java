package com.avysel.blockchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.business.BlockchainParameters;
import com.avysel.blockchain.business.data.IDataFactory;
import com.avysel.blockchain.business.data.custom.DataFactory;

/**
 * Start a blockchain with API.
 */
@SpringBootApplication(scanBasePackages = {"com.avysel.blockchain.api"})
public class ApiBlockchain {
	
	public static void main(String[] args) {
		SpringApplication.run(ApiBlockchain.class, args);
		System.out.println("Starting api");
	}
	
	@Bean
	public Blockchain blockchain() {
		Blockchain blockchain = new Blockchain(new BlockchainParameters());
		blockchain.start();
		return blockchain;
	}
	
	@Bean
	public IDataFactory dataFactory() {
		return new DataFactory();
	}
	
}
