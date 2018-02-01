package com.avysel.blockchain.network.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import org.apache.log4j.Logger;

import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.network.peer.Peer;
import com.avysel.blockchain.network.peer.PeerManager;
import com.avysel.blockchain.tools.JsonMapper;
import com.avysel.blockchain.tools.NetworkTool;

/**
 * Listen to the network to catch new peers connection requests, then add new peers to NetworkManager's peers collection.
 */
public class PeerListener implements Runnable {

	private static Logger log = Logger.getLogger(PeerListener.class);

	private DatagramSocket datagramSocket;
	private PeerManager peerManager;
	private NetworkManager networkManager;
	private boolean running = true;

	public PeerListener(PeerManager manager, NetworkManager networkManager) {
		super();
		this.peerManager = manager;
		this.networkManager = networkManager;
	}

	/**
	 * Start listening network to handle peer's requests.
	 */
	public void start() {

		int port = NetworkTool.getNextAvailablePort(NetworkManager.getBroadcastPort(), 10);

		if(port < 0) {
			log.error("Unable to find unused port.");
			return;
		}

		try {
			this.datagramSocket = new DatagramSocket(port);
			log.info("Peer listener on port "+port);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * Start listening network to handle peers requests.
	 */
	@Override
	public void run() {

		log.debug("Peer listener starts runing.");

		try {
			while(running){

				//On s'occupe maintenant de l'objet paquet
				byte[] buffer = new byte[8192];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

				log.debug("Wait for peer.");
				// wait for data
				datagramSocket.receive(packet);
				log.debug("Get a packet from "+packet.getAddress()+":"+packet.getPort()+". Is it a peer ?");
				log.debug(new String(packet.getData()));

				processPacket(packet);

				//reinit buffer
				packet.setLength(buffer.length);

			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}					
	}


	/**
	 * Process an incoming datagram packet got from a peer.
	 * @param packet the packet to process
	 */
	private void processPacket(DatagramPacket packet) {
		if(packet == null || packet.getData() == null) return;

		NetworkDataBulk bulk = JsonMapper.jsonToBulk(new String(packet.getData()));

		if(bulk != null) {
			
			if(!networkManager.canProcessBulk(bulk.getUid())) {
				return;
			}
			
			switch(bulk.getBulkType()) {

			case NetworkDataBulk.MESSAGE_PEER_HELLO :
				log.debug("New peer on the network, add it.");
				log.info("bulk id :"+bulk.getUid());
				Peer peer = JsonMapper.jsonToPeer(bulk.getBulkData());
				peer.setLastAliveTimestamp(System.currentTimeMillis());
				peer.setIp(packet.getAddress().toString().replace("/", ""));
				peerManager.addPeer(peer);
				answerBack(peer);
				break;			
			default:
				log.warn("Unknown bulk : "+new String(packet.getData()));
				break;
			}
		}
	}

	/**
	 * Stop listening network and peers requests.
	 */
	public void stop() {
		running = false;
	}


	/**
	 * Answer to hello request of a peer. We send it our ip/port/chain height.
	 * @param peer the peer to anwser to.
	 */
	private void answerBack(Peer peer) {
		// create network exploration request 
		NetworkDataBulk bulk = new NetworkDataBulk();
		bulk.setBulkType(NetworkDataBulk.MESSAGE_PEER_HELLO_ANSWER);
		String peerData = JsonMapper.peerToJson(peerManager.getLocalPeer());
		bulk.setBulkData(peerData);	

		// create and send packet to peer
		Socket clientSocket;
		try {
			clientSocket = new Socket(peer.getIp(), peer.getListeningPort());
			log.info("Send back answer to peer's hello.");
			BufferedOutputStream bos = new BufferedOutputStream(clientSocket.getOutputStream());
			bos.write(JsonMapper.bulkToJson(bulk).getBytes());
			bos.flush();	
			bos.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
