package com.avysel.blockchain.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.ISingleData;
import com.avysel.blockchain.model.data.SingleData;
import com.avysel.blockchain.network.client.NodeClient;
import com.avysel.blockchain.network.server.NodeServer;

public class NetworkManager {
	
	private NodeServer server = new NodeServer();
	private NodeClient client = new NodeClient();
	
	private Blockchain blockchain;
	
	private static int port;
	private static String broadcastAddress = "255.255.255.255";
	
	public NetworkManager(Blockchain blockchain) {
		this.blockchain = blockchain;
	}
	
	public void start() {
		// start server and link this manager to the server part, in order to be added to listener that listens network
		server.createNodeServer(this);
	}
		
	/**
	 * Send a data to the network
	 * @param data
	 */
	public void sendData(ISingleData data) {
		DataBulk bulk = new DataBulk();
		
		bulk.setType(DataBulk.DATATYPE_DATA);
		bulk.setData(data.jsonData());
		
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
	
	private void processIncomingData(ISingleData data) {
			blockchain.addIncomingData(data);
	}
	
	private void processIncomingBlock(Block block) {
		// TODO manage incoming block in blockchain
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		NetworkManager.port = port;
	}
	
	
	public static InetAddress getBroadcastAddress() throws UnknownHostException {
		return InetAddress.getByName(broadcastAddress);
	}
	

	public void getIncoming(DataBulk bulk) {
		switch(bulk.getType()) {
		case DataBulk.DATATYPE_BLOCK :
			System.out.println("Get a block from network");
			
			processIncomingBlock(null);
			break;
		case DataBulk.DATATYPE_DATA :
			System.out.println("Get a data from network");
			
			ISingleData data = new SingleData(/* TODO input */null);
			processIncomingData(data); // start adding data to blockchain
			
			break;
		case DataBulk.DATATYPE_CHAIN :
			System.out.println("Get a chain from network");
			// TODO usefull ?
			break;
		}
	}
}