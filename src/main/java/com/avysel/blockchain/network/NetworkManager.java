package com.avysel.blockchain.network;

import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.chain.Chain;
import com.avysel.blockchain.model.data.SingleData;

public class NetworkManager {
	
	public final String DATATYPE_BLOCK = "BLOCK";
	public final String DATATYPE_CHAIN = "CHAIN";
	public final String DATATYPE_DATA = "DATA";
	
	private NodeServer server = new NodeServer();
	private NodeClient client = new NodeClient();
	
	public void start() {
		server.createNodeServer();
	}
	
	/**
	 * Send a data to the network
	 * @param data
	 */
	public void sendData(SingleData data) {
		
	}
	
	/**
	 * Send a @Block to the network
	 * @param block
	 */
	public void sendBlock(Block block) {
		
	}
	
	public void getData(SingleData data) {
		
	}
	
	public void getBlock(Block block) {
		
	}

	/*
	public void sendChain(Chain chain) {
		
	}	
	
	public void getChain(Chain chain) {
		
	}	*/
}
