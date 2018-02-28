package com.avysel.blockchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.avysel.blockchain.business.Blockchain;

/**
 * Start a blockchain with API.
 */
@SpringBootApplication(scanBasePackages = {"com.avysel.blockchain.api"})
public class ApiBlockchain {
	
	public static void main(String[] args) {
		SpringApplication.run(ApiBlockchain.class, args);
		
		Blockchain blockchain = new Blockchain();
		blockchain.start();
	}
	
	@Bean
	public Blockchain blockchain() {
		Blockchain blockchain = new Blockchain();
		blockchain.start();
		return blockchain;
	}
	
}
