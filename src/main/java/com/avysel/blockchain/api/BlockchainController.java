package com.avysel.blockchain.api;

import javax.servlet.http.HttpServletResponse;

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
import com.avysel.blockchain.business.BlockchainStatus;
import com.avysel.blockchain.business.block.Block;
import com.avysel.blockchain.business.data.IDataFactory;
import com.avysel.blockchain.business.data.ISingleData;
import com.avysel.blockchain.mining.DataPool;

@Controller
@EnableAutoConfiguration
@RequestMapping(value = {"/blockchain/api"})
public class BlockchainController {

	@Autowired
	Blockchain blockchain;
	
	@Autowired
	IDataFactory dataFactory;
	
	@RequestMapping(value="/status", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public BlockchainStatus status() {	
		return blockchain.getStatus();
	}

	@RequestMapping(value="/data/pool", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public DataPool dataPool() {	
		return blockchain.getDataPool();
	}	
	
	@RequestMapping(value="/data", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE )
	public void newData (@RequestBody String dataValue, HttpServletResponse response) {
		ISingleData data = dataFactory.createData(dataValue);
		try {
			blockchain.addData(data);
			response.setStatus(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
			response.setStatus(500);
		}
	}
	
	@RequestMapping(value="/data/{dataHash}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public Block getData (@PathVariable String dataHash) {
        return BlockchainManager.findBlockByData(blockchain.getChain(), dataHash);
	}
}
