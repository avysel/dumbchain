package com.avysel.blockchain.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.SingleData;
import com.avysel.blockchain.network.client.NodeClient;
import com.avysel.blockchain.network.client.PeerExplorer;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.network.peer.Peer;
import com.avysel.blockchain.network.server.NodeServer;
import com.avysel.blockchain.network.server.PeerListener;
import com.avysel.blockchain.tools.JsonMapper;

public class NetworkManager {

	private static Logger log = Logger.getLogger("com.avysel.blockchain.network.NetworkManager");
	
	private NodeServer server;
	private NodeClient client;
	private List<Peer> peers;
	private PeerExplorer peerExplorer;
	private PeerListener peerListener;

	private Blockchain blockchain;

	private static int port = 45458;
	//private static String broadcastAddress = "255.255.255.255";
	private static String broadcastAddress = "127.0.0.1";

	public NetworkManager(Blockchain blockchain) {
		this.blockchain = blockchain;
		server = new NodeServer(this);
		client = new NodeClient(this);
		peers = new ArrayList<Peer>();
		peerExplorer = new PeerExplorer(this);
		peerListener = new PeerListener(this);		
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
		server.start();
		peerExplorer.start();
		peerListener.start();
	}

	public void stop() {
		server.stop();
		peerExplorer.stop();
		peerListener.stop();
	}

	/**
	 * Send a data to the network
	 * @param data
	 */
	public void sendData(SingleData data) {
		NetworkDataBulk bulk = new NetworkDataBulk();

		// create network packet
		bulk.setType(NetworkDataBulk.DATATYPE_DATA);
		bulk.setData(data.getData());

		// send to all connected peers
		client.sendDataToAllPeers(bulk);
	}

	/**
	 * Send a @Block to the network
	 * @param block
	 */
	public void sendBlock(Block block) {
		NetworkDataBulk bulk = new NetworkDataBulk();

		bulk.setType(NetworkDataBulk.DATATYPE_BLOCK);
		bulk.setData(block.getStringData());

		client.sendDataToAllPeers(bulk);
	}

	private void processIncomingData(SingleData data) {
		log.info("Pending before : "+blockchain.pendingData.size());
		try {
			blockchain.addIncomingData(data);
		} catch (InterruptedException e) {
			// TODO what to do when data not added ?
			e.printStackTrace();
		}
		log.info("Pending after : "+blockchain.pendingData.size());
	}

	private void processIncomingBlock(Block block) {
		// TODO manage incoming block in blockchain + manage forks
	}

	/**
	 * Gets data from network, transform it into @SingleData or @Block and add it to the @Blockchain
	 * @param bulk the incoming @DataBulk
	 */
	public void getIncoming(NetworkDataBulk bulk) {
		switch(bulk.getType()) {
		case NetworkDataBulk.DATATYPE_BLOCK :
			log.info("Get a block from network");
			Block block = JsonMapper.jsonToBlock(bulk.getData());
			processIncomingBlock(block);
			break;
		case NetworkDataBulk.DATATYPE_DATA :
			log.info("Get a data from network");
			SingleData data = JsonMapper.jsonToData(bulk.getData());
			processIncomingData(data);
			break;
		case NetworkDataBulk.DATATYPE_CHAIN :
			log.info("Get a chain from network");
			// TODO usefull ?
			break;
		default: 
			System.out.println("error incoming, unkown type");
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
