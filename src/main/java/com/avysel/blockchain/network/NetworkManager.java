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
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.network.peer.Peer;
import com.avysel.blockchain.network.peer.PeerManager;
import com.avysel.blockchain.network.server.NodeServer;
import com.avysel.blockchain.tools.JsonMapper;

/**
 * Handle all the network part of the current Blockchain.
 * This Manager creates and maintains server connections to be connected to other network nodes (Peers).
 * It's the link between network and current Chain. It handles process of incoming and outgoing data.
 *
 */
public class NetworkManager {

	private static Logger log = Logger.getLogger("com.avysel.blockchain.network.NetworkManager");

	private NodeServer server;
	private NodeClient client;
	private PeerManager peerManager;


	private Blockchain blockchain;

	private static int serverListeningPort = 0;
	private static int broadcastPort = 45458;
	//private static String broadcastAddress = "255.255.255.255";
	private static String broadcastAddress = "127.0.0.1";

	/**
	 * Number of second from last contact with peer to consider it as still alive.
	 */
	public static long DEFAULT_PEER_STILL_ALIVE = 3600;

	/**
	 * Maximum number of peers to a peer asking for connections.
	 */
	public static long DEFAULT_PEER_NUMBER_TO_SEND = 10;

	public NetworkManager(Blockchain blockchain) {
		this.blockchain = blockchain;
		server = new NodeServer(this);
		client = new NodeClient(this);

		peerManager = new PeerManager();
	}

	/**
	 * Provides the port used by current server
	 * @return the server port
	 */
	public static int getServerListeningPort() {	
		if(serverListeningPort == 0) {
			log.error("Server port not initialized");
		}
		return serverListeningPort; 
	}

	/**
	 * Set the listening port of the current server
	 * @param port the port
	 */
	public static void setServerListeningPort(int port) {		serverListeningPort = port;	}

	/**
	 * Provides the InetAddress used to send broadcast hello message when a blockchain node is started
	 * @return the InetAddress that represents broadcast IP address
	 * @throws UnknownHostException
	 */
	public static InetAddress getBroadcastAddress() throws UnknownHostException {	
		return InetAddress.getByName(broadcastAddress);	
	}

	/**
	 * Provides the port used to send hello messages when a blockchain node is started
	 * @return
	 */
	public static int getBroadcastPort() {		
		return broadcastPort;	
	}



	/**
	 * Provides all InetAddress that can be used on the current network to broadcast data
	 * @return a List of broadcast InetAddress
	 * @throws SocketException
	 */
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

	/**
	 * Start network manager of blockchain node
	 */
	public void start() {
		server.start();
		peerManager.start();
	}

	/**
	 * Stop network manager of blockchain node
	 */
	public void stop() {
		server.stop();
		peerManager.stop();
	}

	/**
	 * Send a data to the network
	 * @param data the SingleData object to send
	 */
	public void sendData(SingleData data) {
		log.info("Send a data to the network.");
		log.trace(data.toString());
		NetworkDataBulk bulk = new NetworkDataBulk();

		// create network packet
		bulk.setBulkType(NetworkDataBulk.DATATYPE_DATA);
		bulk.setBulkData(data.getData());

		// send to all connected peers
		client.sendDataToAllPeers(bulk);
	}

	/**
	 * Send a @Block to the network
	 * @param block the Block object to send
	 */
	public void sendBlock(Block block) {
		log.info("Send a block to the network.");
		log.trace(block.toString());
		NetworkDataBulk bulk = new NetworkDataBulk();

		bulk.setBulkType(NetworkDataBulk.DATATYPE_BLOCK);
		bulk.setBulkData(JsonMapper.blockToJson(block));

		client.sendDataToAllPeers(bulk);
	}

	/**
	 * Process an incoming SingleData received from the network. Add it to the DataPool if not already exists
	 * @param data the incoming SingleData
	 */
	private void processIncomingData(SingleData data) {
		log.info("Pool size before : "+blockchain.getDataPool().size());
		try {
			blockchain.addIncomingData(data);
		} catch (InterruptedException e) {
			// TODO what to do when data not added ?
			e.printStackTrace();
		}
		log.info("Pool size after : "+blockchain.getDataPool().size());
	}

	/**
	 * Process an incoming Block received from the network. Add it to the Chain according to rules about consensus and forks.
	 * @param data the incoming Block
	 */
	private void processIncomingBlock(Block block) {
		blockchain.addIncomingBlock(block);
	}

	/**
	 * Gets data from network, transform it into @SingleData or @Block and add it to the @Blockchain
	 * @param bulk the incoming @DataBulk
	 */
	public void processIncoming(NetworkDataBulk bulk) {
		switch(bulk.getBulkType()) {
		case NetworkDataBulk.DATATYPE_BLOCK :
			log.info("Get a block from network");
			log.info(bulk);
			Block block = JsonMapper.jsonToBlock(bulk.getBulkData());
			processIncomingBlock(block);
			break;
		case NetworkDataBulk.DATATYPE_DATA :
			log.info("Get a data from network");
			log.info(bulk);
			SingleData data = JsonMapper.jsonToData(bulk.getBulkData());
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

	/**
	 * Returns the Peer representing the current node
	 * @return the local Peer
	 */
	public Peer getLocalPeer() {
		return peerManager.getLocalPeer();
	}

	/**
	 * Sets the Peer represening the current node
	 * @param localPeer the current node's Peer representation
	 */
	public void setLocalPeer(Peer localPeer) {
		this.peerManager.setLocalPeer(localPeer);
	}	

	/**
	 * Provides all Peers connected to current node.
	 * @return
	 */
	public List<Peer> getPeers() {
		return peerManager.getPeersList();
	}
	
	/**
	 * Returns the list of peers we had contact with in less than NetworkManager.DEFAULT_PEER_STILL_ALIVE seconds ago.
	 * @return the list of peers considered as still alive
	 */
	public List<Peer> getAlivePeers() {
		return peerManager.getAlivePeers();
	}
	
	public void markPeerAlive(String ip, int port) {
		peerManager.markPeerAsAlive(ip, port);
	}
}