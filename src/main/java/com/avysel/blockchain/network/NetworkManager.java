package com.avysel.blockchain.network;

import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.SingleData;

public class NetworkManager {
	
	private NodeServer server = new NodeServer();
	private NodeClient client = new NodeClient();
	
	private static int port;
	
	public void start() {
		server.createNodeServer();
	}
	
	/**
	 * Send a data to the network
	 * @param data
	 */
	public void sendData(SingleData data) {
		DataBulk bulk = new DataBulk();
		
		bulk.setType(DataBulk.DATATYPE_DATA);
		bulk.setData(data.getData());
		
		client.broadcast(bulk);
	}
	
	/**
	 * Send a @Block to the network
	 * @param block
	 */
	public void sendBlock(Block block) {
		DataBulk bulk = new DataBulk();
		
		bulk.setType(DataBulk.DATATYPE_BLOCK);
		bulk.setData(block.getStringData());
		
		client.broadcast(bulk);
	}
	
	public void getData(SingleData data) {
		
	}
	
	public void getBlock(Block block) {
		
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		NetworkManager.port = port;
	}

}
