package com.avysel.blockchain.network.client;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.List;

import org.apache.log4j.Logger;

import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.network.peer.Peer;
import com.avysel.blockchain.tools.JsonMapper;

public class NodeClient {

	private static Logger log = Logger.getLogger("com.avysel.blockchain.network.client.NodeClient");

	private Socket clientSocket;
	private NetworkManager networkManager;

	public NodeClient(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	public void sendDataToAllPeers(NetworkDataBulk bulk) {

		// send data to all alive peers
		List<Peer> peers = networkManager.getAlivePeers();
		log.debug("Nb alive peers : "+peers.size());
		for(Peer peer : peers) {
			sendData(bulk, peer);
		}
	}

	public void sendData(NetworkDataBulk bulk, Peer peer) {
		if(networkManager.getLocalPeer() != null) {
			bulk.setSender(networkManager.getLocalPeer());
		}
		else {
			log.error("No local peer, unable to set sender data");
		}

		log.debug("Sending data "+bulk+" to "+peer.getIp()+":"+peer.getPort());

		try {
			// connect to distant peer's server part
			try {
				clientSocket = new Socket(peer.getIp(), peer.getPort());
			}
			catch(ConnectException e) {
				log.debug("Connection failed, remove peer "+peer);
				networkManager.removePeer(peer);
			}
			// send data
			BufferedOutputStream bos = new BufferedOutputStream(clientSocket.getOutputStream());
			bos.write(JsonMapper.bulkToJson(bulk).getBytes());
			bos.flush();
			bos.close();
			clientSocket.close();

		} catch (IOException e) {
			log.error("Data not send to "+peer.getIp()+":"+peer.getPort());
			e.printStackTrace();
		}
	}
}
