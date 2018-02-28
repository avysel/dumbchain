package com.avysel.blockchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.avysel.blockchain.api"})
public class ApiBlockchain {
	
	public static void main(String[] args) {
		SpringApplication.run(ApiBlockchain.class, args);
		Main.main(args);
	}
	
}
