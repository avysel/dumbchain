package com.avysel.blockchain.api;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avysel.blockchain.business.data.ISingleData;

@Controller
@EnableAutoConfiguration
@RequestMapping(value = {"/blockchain/api"})
public class BlockchainController {

	@RequestMapping(value="/data", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE )
	@ResponseBody
	public void newData (@RequestBody String requestJson) {

	}

	
	@RequestMapping(value="/data/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ISingleData getData (@PathVariable String dataId) {
        return null;
	}	
	
}
