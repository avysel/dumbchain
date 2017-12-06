package com.avysel.blockchain.network.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import com.avysel.blockchain.network.DataBulk;
import com.avysel.blockchain.network.NetworkManager;

public class NodeClient {

	private DatagramSocket clientSocket;

	public void broadcast(DataBulk bulk) {
		try {
			
			// create broadcast socket
			clientSocket = new DatagramSocket();
			clientSocket.setBroadcast(true);
			
			String data = bulk.toString(); // TODO to json
			
			// create and send packet
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

	// TODO how to use ?
	List<InetAddress> listAllBroadcastAddresses() throws SocketException {
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
}
