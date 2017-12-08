package com.avysel.blockchain.network.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.avysel.blockchain.network.DataBulk;
import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.tools.JsonMapper;

public class NodeClient {

	private DatagramSocket clientSocket;

	public void broadcast(DataBulk bulk) {
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
