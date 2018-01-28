package com.avysel.blockchain.network.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.log4j.Logger;

import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.network.peer.PeerManager;
import com.avysel.blockchain.tools.JsonMapper;
import com.avysel.blockchain.tools.NetworkTool;

/**
 * Broadcast connection request trough network, expected distant peer's answer and create connections
 */
public class PeerExplorer {

	private static Logger log = Logger.getLogger(PeerExplorer.class);

	private PeerManager peerManager;

	public PeerExplorer(PeerManager manager) {
		super();
		this.peerManager = manager;
	}	

	/**
	 * Send a broadcast message to network to introduce itself as a new node
	 */
	public void hello() {

		log.info("Local peer says hello.");

		try {

			DatagramSocket clientSocket = new DatagramSocket();
			// create broadcast socket
			clientSocket.setBroadcast(true);

			// create network exploration request 
			NetworkDataBulk bulk = new NetworkDataBulk();
			bulk.setBulkType(NetworkDataBulk.MESSAGE_PEER_HELLO);
			String peerData = JsonMapper.peerToJson(peerManager.getLocalPeer());
			bulk.setBulkData(peerData);
			String data = JsonMapper.bulkToJson(bulk);

			List<InetAddress> listAddresses = NetworkTool.listAllBroadcastAddresses();

			for(InetAddress addr : listAddresses) {
				for(int i = 0 ; i < 10 ; i ++) {

					// create and send packet // TODO listAllBroadcastAddresses
					DatagramPacket packet = new DatagramPacket(
							data.getBytes()
							, data.getBytes().length
							, addr
							, NetworkManager.getBroadcastPort() + i
							);

					log.debug("Send connection request to network.");
					clientSocket.send(packet);
				}
			}
			clientSocket.close();

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
