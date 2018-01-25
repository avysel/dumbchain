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
 * Listen to the network to catch new peers connection requests, then add new peers to NetworkManager's peers collection
 */
public class PeerListener implements Runnable {

	private static Logger log = Logger.getLogger("com.avysel.blockchain.network.server.PeerListener");

	private DatagramSocket datagramSocket;
	private PeerManager peerManager;
	private boolean running = true;

	public PeerListener(PeerManager manager) {
		super();
		this.peerManager = manager;
	}

	public void start() {
		
		int port = NetworkTool.getNextAvailablePort(NetworkManager.getBroadcastPort(), 10);
		
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
	 * Start listening network
	 */
	@Override
	public void run() {

		Thread t = new Thread(new Runnable(){
			public void run(){
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
						log.debug(packet.getData().toString());

						processData(packet);

						//reinit buffer
						packet.setLength(buffer.length);

					}
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}					
			}

		});

		t.start();
	}	

	private void processData(DatagramPacket packet) {
		if(packet == null || packet.getData() == null) return;
		
		NetworkDataBulk bulk = JsonMapper.jsonToBulk(new String(packet.getData()));

		if(bulk != null) {
			switch(bulk.getBulkType()) {

			case NetworkDataBulk.MESSAGE_PEER_HELLO :
				log.debug("New peer on the network, add it.");
				Peer peer = JsonMapper.jsonToPeer(bulk.getBulkData());
				peer.setLastAliveTimestamp(System.currentTimeMillis());
				peer.setIp(packet.getAddress().toString().replace("/",""));
				peerManager.addPeer(peer);
				answerBack(peer);
				break;			
			default:
				log.warn("Unknown bulk : "+new String(packet.getData()));
				break;
			}
		}
	}

	public void stop() {
		running = false;
	}

	
	private void answerBack(Peer peer) {
		// create network exploration request 
		NetworkDataBulk bulk = new NetworkDataBulk();
		bulk.setBulkType(NetworkDataBulk.MESSAGE_PEER_HELLO_ANSWER);
		String peerData = JsonMapper.peerToJson(peerManager.getLocalPeer());
		bulk.setBulkData(peerData);	
		
		// create and send packet to peer
		Socket clientSocket;
		try {
			clientSocket = new Socket(peer.getIp(), peer.getPort());
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
