package com.avysel.blockchain.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.business.BlockchainManager;
import com.avysel.blockchain.business.block.Block;
import com.avysel.blockchain.business.data.IDataFactory;
import com.avysel.blockchain.business.data.ISingleData;

@Controller
@EnableAutoConfiguration
@RequestMapping(value = {"/blockchain/api"})
public class BlockchainController {

	@Autowired
	Blockchain blockchain;
	
	@Autowired
	IDataFactory dataFactory;
	
	@RequestMapping(value="/health")
	public String health() {
		
		System.out.println(blockchain);
		
		return "ok";
	}
	
	@RequestMapping(value="/data", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE )
	public void newData (@RequestBody String dataValue) {
		ISingleData data = dataFactory.getDataInstance(dataValue);
		try {
			blockchain.addData(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	@RequestMapping(value="/data/{hash}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public Block getData (@PathVariable String dataHash) {
        return BlockchainManager.findBlockByData(blockchain.getChain(), dataHash);
	}
	
}
