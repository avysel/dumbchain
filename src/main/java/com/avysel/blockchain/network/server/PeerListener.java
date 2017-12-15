package com.avysel.blockchain.network.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.log4j.Logger;

import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.network.peer.Peer;
import com.avysel.blockchain.network.peer.PeerManager;
import com.avysel.blockchain.tools.JsonMapper;

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
		
		try {
			this.datagramSocket = new DatagramSocket(NetworkManager.getBroadcastPort());
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
				log.info("Peer listener starts runing ...");

				try {
					while(running){

						//On s'occupe maintenant de l'objet paquet
						byte[] buffer = new byte[8192];
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

						log.info("Wait for peer ...");
						// wait for data
						datagramSocket.receive(packet);
						log.info("Get a packet from "+packet.getAddress()+":"+packet.getPort()+". Is it a peer ?");

						// read
						String str = new String(packet.getData());
						log.info(str);

						processData(str);

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

	private void processData(String data) {
		NetworkDataBulk bulk = JsonMapper.jsonToBulk(data);

		if(bulk != null) {
			switch(bulk.getBulkType()) {

			case NetworkDataBulk.MESSAGE_PEER_HELLO :
				log.info("New peer on the network, add it.");
				Peer peer = JsonMapper.jsonToPeer(bulk.getBulkData());
				peerManager.addPeer(peer);
				break;
			default:
				log.warn("Unknown bulk : "+data);
				break;
			}
		}
	}

	public void stop() {
		running = false;
	}

}
