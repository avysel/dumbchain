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
	private Peer localPeer;

	private Blockchain blockchain;

	private static int port = 45458;
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
		peers = new ArrayList<Peer>();
		peerExplorer = new PeerExplorer(this);
		peerListener = new PeerListener(this);	
		localPeer = Peer.initFromLocal();
	}

	public static int getPort() {	return port; }
	public static void setPort(int port) {		NetworkManager.port = port;	}	
	public static InetAddress getBroadcastAddress() throws UnknownHostException {	return InetAddress.getByName(broadcastAddress);	}

	public Peer getLocalPeer() {
		return localPeer;
	}

	public void setLocalPeer(Peer localPeer) {
		this.localPeer = localPeer;
	}

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
		peerExplorer.wakeUp();
		peerListener.start();
	}

	/**
	 * Stop network manager of blockchain node
	 */
	public void stop() {
		server.stop();
		peerListener.stop();
	}

	/**
	 * Send a data to the network
	 * @param data
	 */
	public void sendData(SingleData data) {
		log.info("Send a data to the network.");
		log.trace(data.toString());
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
		log.info("Send a block to the network.");
		log.trace(block.toString());
		NetworkDataBulk bulk = new NetworkDataBulk();

		bulk.setType(NetworkDataBulk.DATATYPE_BLOCK);
		bulk.setData(block.getStringData());

		client.sendDataToAllPeers(bulk);
	}

	private void processIncomingData(SingleData data) {
		log.info("Pending before : "+blockchain.getDataPool().size());
		try {
			blockchain.addIncomingData(data);
		} catch (InterruptedException e) {
			// TODO what to do when data not added ?
			e.printStackTrace();
		}
		log.info("Pending after : "+blockchain.getDataPool().size());
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
		log.info("New peer added : "+peer);
	}

	public void removePeer(Peer peer) {
		peers.remove(peer);
	}
	
	public boolean isLocalPeer(Peer peer) {
		return this.localPeer.getUid().equals(peer.getUid());
	}	
	
	/**
	 * Get peers that are still alive
	 * @return list of alive peers
	 */
	public List<Peer> getAlivePeers() {
		List<Peer> peers = new ArrayList<Peer>();
		// TODO
		return peers;
	}
	
	private void cleanPeersList() {
		// TODO ping all peers to keep them alive
	}
}
