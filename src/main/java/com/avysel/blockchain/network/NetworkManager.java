package com.avysel.blockchain.network;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.SingleData;

public class NetworkManager {
	
	private NodeServer server = new NodeServer();
	private NodeClient client = new NodeClient();
	
	private Blockchain blockchain;
	
	private static int port;
	
	public NetworkManager(Blockchain blockchain) {
		this.blockchain = blockchain;
	}
	
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
	
	public void incomingData(SingleData data) {
		blockchain.addIncomingData(data);
	}
	
	public void incomingBlock(Block block) {
		// TODO manage incoming block in blockchain
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		NetworkManager.port = port;
	}
	
	public static void getIncoming(DataBulk bulk) {
		
	}
}
