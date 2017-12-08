package com.avysel.blockchain.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.SingleData;
import com.avysel.blockchain.network.client.NodeClient;
import com.avysel.blockchain.network.server.NodeServer;
import com.avysel.blockchain.tools.JsonMapper;

public class NetworkManager {

	private NodeServer server = new NodeServer();
	private NodeClient client = new NodeClient();
	private List<Peer> peers = new ArrayList<Peer>();

	private Blockchain blockchain;

	private static int port = 45458;
	//private static String broadcastAddress = "255.255.255.255";
	private static String broadcastAddress = "127.0.0.1";

	public NetworkManager(Blockchain blockchain) {
		this.blockchain = blockchain;
	}

	public static int getPort() {	return port; }
	public static void setPort(int port) {		NetworkManager.port = port;	}	
	public static InetAddress getBroadcastAddress() throws UnknownHostException {	return InetAddress.getByName(broadcastAddress);	}

	public List<InetAddress> listAllBroadcastAddresses() throws SocketException {
		List<InetAddress> broadcastList = new ArrayList<>();
		Enumeration<NetworkInterface> interfaces 
		= NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();

			if (networkInterface.isLoopback() || !networkInterface.isUp()) {
				continue;
			}

			networkInterface.getInterfaceAddresses().stream() 
			.map(a -> a.getBroadcast())
			.filter(Objects::nonNull)
			.forEach(broadcastList::add);
		}
		return broadcastList;
	}	

	public void start() {
		server.createNodeServer(this);
	}

	public void stop() {
		server.stop();
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

	private void processIncomingData(SingleData data) {
		System.out.println("Pending before : "+blockchain.pendingData.size());
		try {
			blockchain.addIncomingData(data);
		} catch (InterruptedException e) {
			// TODO what to do when data not added
			e.printStackTrace();
		}
		System.out.println("Pending after : "+blockchain.pendingData.size());
	}

	private void processIncomingBlock(Block block) {
		// TODO manage incoming block in blockchain + manage forks
	}

	/**
	 * Gets data from network, transform it into @ISingleData or @Block and add it to the @Blockchain
	 * @param bulk the incoming @DataBulk
	 */
	public void getIncoming(DataBulk bulk) {
		switch(bulk.getType()) {
		case DataBulk.DATATYPE_BLOCK :
			System.out.println("Get a block from network");
			Block block = JsonMapper.jsonToBlock(bulk.getData());
			processIncomingBlock(block);
			break;
		case DataBulk.DATATYPE_DATA :
			System.out.println("Get a data from network");
			SingleData data = JsonMapper.jsonToData(bulk.getData());
			processIncomingData(data);
			break;
		case DataBulk.DATATYPE_CHAIN :
			System.out.println("Get a chain from network");
			// TODO usefull ?
			break;
		}
	}
	
	public List<Peer> getPeers() {
		return peers;
	}
	
	public void addPeer(Peer peer) {
		peers.add(peer);
	}
	
	public void removePeer(Peer peer) {
		peers.remove(peer);
	}
}
