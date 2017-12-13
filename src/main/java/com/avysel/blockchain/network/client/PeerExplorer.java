package com.avysel.blockchain.network.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.tools.JsonMapper;

/**
 * Broadcast connection request trough network, expected distant peer's answer and create connections
 */
public class PeerExplorer {

	private static Logger log = Logger.getLogger("com.avysel.blockchain.network.client.PeerExplorer");
	
	private NetworkManager manager;

	public PeerExplorer(NetworkManager manager) {
		super();
		this.manager = manager;
	}	

	/**
	 * Send a broadcast message to network to introduce itself as a new node
	 */
	public void wakeUp() {

		log.info("Local peer says hello.");
		
		try {

			DatagramSocket clientSocket = new DatagramSocket();
			// create broadcast socket
			clientSocket.setBroadcast(true);

			// create network exploration request 
			NetworkDataBulk bulk = new NetworkDataBulk();
			bulk.setBulkType(NetworkDataBulk.MESSAGE_PEER_HELLO);
			String peerData = JsonMapper.peerToJson(manager.getLocalPeer());
			bulk.setBulkData(peerData);
			String data = JsonMapper.bulkToJson(bulk);

			log.info(peerData);
			
			// create and send packet // TODO listAllBroadcastAddresses
			DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, NetworkManager.getBroadcastAddress(), NetworkManager.getBroadcastPort());

			log.info("Send connection request to network.");
			clientSocket.send(packet);

			clientSocket.close();

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			// close ?
		}
	}
}
