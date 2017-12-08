package com.avysel.blockchain.network.client;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import com.avysel.blockchain.network.NetworkDataBulk;
import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.Peer;
import com.avysel.blockchain.tools.JsonMapper;

public class NodeClient {

	private Socket clientSocket;
	private NetworkManager networkManager;
	
	public NodeClient(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}
	
	public void sendDataToAllPeers(NetworkDataBulk bulk) {
		
		// send data to all peers
		List<Peer> peers = networkManager.getPeers();
		for(Peer peer : peers) {
			sendData(bulk, peer);
		}
	}
	
	public void sendData(NetworkDataBulk bulk, Peer peer) {
		try {
			// connect to distant peer's server part
			clientSocket = new Socket(peer.getIp(), peer.getPort());
			
			// send data
			BufferedOutputStream bos = new BufferedOutputStream(clientSocket.getOutputStream());
			bos.write(JsonMapper.bulkToJson(bulk).getBytes());
			bos.flush();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	public void broadcast(NetworkDataBulk bulk) {
		try {

			// create broadcast socket
			clientSocket = new DatagramSocket();
			clientSocket.setBroadcast(true);

			String data = JsonMapper.bulkToJson(bulk);

			// create and send packet // TODO listAllBroadcastAddresses
			DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, NetworkManager.getBroadcastAddress(), NetworkManager.getPort());
			clientSocket.send(packet);

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
