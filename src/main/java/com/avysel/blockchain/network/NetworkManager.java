package com.avysel.blockchain.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.ISingleData;
import com.avysel.blockchain.model.data.SingleData;
import com.avysel.blockchain.network.client.NodeClient;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.network.data.message.CatchUpDataMessage;
import com.avysel.blockchain.network.data.message.CatchUpRequestMessage;
import com.avysel.blockchain.network.data.message.NetworkMessage;
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

	private static Logger log = Logger.getLogger(NetworkManager.class);

	private NodeServer server;
	private NodeClient client;
	private PeerManager peerManager;


	private Blockchain blockchain;

	private static int serverListeningPort = 0;
	private static int broadcastPort = 45458;

	private static List<String> receivedBulks;

	/**
	 * Number of second from last contact with peer to consider it as still alive.
	 */
	public static long DEFAULT_PEER_STILL_ALIVE = 3600;

	public NetworkManager(Blockchain blockchain) {
		this.blockchain = blockchain;
		server = new NodeServer(this);
		client = new NodeClient(this);

		peerManager = new PeerManager();

		receivedBulks = new ArrayList<String>();
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
	 * Provides the port used to send hello messages when a blockchain node is started
	 * @return the broadcast port
	 */
	public static int getBroadcastPort() {		
		return broadcastPort;	
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
	public void sendData(ISingleData data) {
		log.info(">>>>> Send a data to the network.");
		log.trace(data.toString());
		NetworkDataBulk bulk = new NetworkDataBulk();

		// create network packet
		bulk.setBulkType(NetworkDataBulk.DATATYPE_DATA);
		bulk.setBulkData(JsonMapper.dataToJson(data));

		// send to all connected peers
		client.sendDataToAllPeers(bulk);
	}

	/**
	 * Send a Block to the network
	 * @param block the Block object to send
	 */
	public void sendBlock(Block block) {
		log.info(">>>>>>>>>> Send a block to the network : "+block.getIndex());
		log.trace(block.toString());
		NetworkDataBulk bulk = new NetworkDataBulk();

		bulk.setBulkType(NetworkDataBulk.DATATYPE_BLOCK);
		bulk.setBulkData(JsonMapper.blockToJson(block));

		client.sendDataToAllPeers(bulk);
	}

	/**
	 * Send a NetworkMessage to a Peer
	 * @param bulkType the type of bulk
	 * @param message the message object to send
	 * @param peer the recipient
	 */
	public void sendMessage(int bulkType, NetworkMessage message, Peer peer) {
		log.debug("Send a message to "+peer);
		if (message != null) log.trace(message.toString());
		NetworkDataBulk bulk = new NetworkDataBulk();

		bulk.setBulkType(bulkType);
		bulk.setBulkData(JsonMapper.messageToJson(message));

		client.sendData(bulk, peer);
	}

	/**
	 * Process an incoming SingleData received from the network. Add it to the DataPool if not already exists
	 * @param data the incoming SingleData
	 */
	private void processIncomingData(SingleData data) {
		log.info(" <<<<< Incoming data : "+ data);
		synchronized(blockchain.getDataPool()) {
			log.debug("Pool size before : "+blockchain.getDataPool().size());
			try {
				blockchain.addIncomingData(data);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log.debug("Pool size after : "+blockchain.getDataPool().size());
		}
	}

	/**
	 * Process an incoming Block received from the network. Add it to the Chain according to rules about consensus and forks.
	 * @param data the incoming Block
	 */
	private void processIncomingBlock(Block block) {
		log.info(" <<<<<<<<<< Incoming block : "+ block.getIndex() + " ("+block.getHash()+")");
		blockchain.addIncomingBlock(block);
	}

	/**
	 * Gets data from network, transform it into SingleData or Block and add it to the Blockchain
	 * @param bulk the incoming DataBulk
	 * @param senderIp IP address of sender node
	 */
	public void processIncoming(NetworkDataBulk bulk, String senderIp) {
		log.debug("Incoming Bulk : "+bulk);
		if(bulk.getSender() != null)
			markPeerAsAlive(bulk.getSender().getUid());
		// make sure we don't process twice the same message if got twice
		if(receivedBulks.contains(bulk.getUid())) {
			log.info("Incoming bulk already received.");
			return;
		}
		else {
			receivedBulks.add(bulk.getUid());
		}

		switch(bulk.getBulkType()) {
		case NetworkDataBulk.DATATYPE_BLOCK :
			log.debug("Get a block from network");
			Block block = JsonMapper.jsonToBlock(bulk.getBulkData());
			processIncomingBlock(block);
			break;
		case NetworkDataBulk.DATATYPE_DATA :
			log.debug("Get a data from network");
			SingleData data = JsonMapper.jsonToData(bulk.getBulkData());
			log.debug(data);
			processIncomingData(data);
			break;
		case NetworkDataBulk.MESSAGE_PEER_HELLO_ANSWER :
			log.debug("A peer answered to hello, add it.");
			Peer peer = JsonMapper.jsonToPeer(bulk.getBulkData());
			peer.setIp(senderIp); // sender only knows its local ip, change it to its public ip
			peer.setLastAliveTimestamp(System.currentTimeMillis());
			peerManager.addPeer(peer);
			break;		
		case NetworkDataBulk.MESSAGE_CATCH_UP_REQUEST :
			log.debug("Incoming catch-up request");
			CatchUpRequestMessage requestMessage = JsonMapper.jsonToCatchUpRequestMessage(bulk.getBulkData());
			blockchain.sendCatchUp(bulk.getSender(), requestMessage.getStartIndex());
			break;				
		case NetworkDataBulk.MESSAGE_CATCH_UP_BLOCKS :
			log.debug("Get catch up data");
			CatchUpDataMessage dataMessage = JsonMapper.jsonToCatchUpDataMessage(bulk.getBulkData());
			List<Block> blocks = dataMessage.getBlocks();
			blockchain.addCatchUp(blocks);
			break;
		case NetworkDataBulk.MESSAGE_CATCH_UP_EMPTY :
			log.debug("Get an empty catch-up");
			blockchain.emptyCatchUp();
			break;			
		default: 
			log.error("error incoming, unkown type : "+bulk.getBulkType());
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
	 * Sets the Peer representing the current node
	 * @param localPeer the current node's Peer representation
	 */
	public void setLocalPeer(Peer localPeer) {
		this.peerManager.setLocalPeer(localPeer);
	}	

	/**
	 * Provides all Peers connected to current node.
	 * @return the list of all peers
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

	/**
	 * Mark a peer as alive. It store the current timestamp as the last time this peer gaves us a sign of life.
	 * @param peerId the ID of the peer.
	 */
	public void markPeerAsAlive(String peerId) {
		peerManager.markPeerAsAlive(peerId);
	}

	/**
	 * Remove a peer from the peers list.
	 * @param peer the peer to remove.
	 */
	public void removePeer(Peer peer) {
		peerManager.removePeer(peer);
	}
}