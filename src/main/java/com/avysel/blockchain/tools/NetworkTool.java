package com.avysel.blockchain.tools;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;


import com.avysel.blockchain.network.peer.Peer;

public class NetworkTool {

	public static void send(String data, Peer to) {
		// TODO use
	}
	
	public static int getNextAvailablePort(int startPort, int maxTries) {
		for(int p = startPort ; p < startPort + maxTries ; p++) {
			if(!isPortInUse(p)) {
				return p;
			}
		}

		return -1;
	}

	private static boolean isPortInUse(int portNumber) {
		try {
			DatagramSocket s = new DatagramSocket(portNumber);
			s.close();
			return false;
		}
		catch(Exception e) {
			return true;
		}
	}
	
	public static String getLocalIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress() ;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getBroadcastIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress() ;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Provides all InetAddress that can be used on the current network to broadcast data
	 * @return a List of broadcast InetAddress
	 * @throws SocketException when connection error occurs
	 */
	public static List<InetAddress> listAllBroadcastAddresses() throws SocketException {
		List<InetAddress> broadcastList = new ArrayList<>();
		try {
			broadcastList.add(InetAddress.getByName("127.0.0.1"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		/*
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();

			if (networkInterface.isLoopback() || !networkInterface.isUp()) {
				continue;
			}

			networkInterface.getInterfaceAddresses().stream() 
			.map(a -> a.getBroadcast())
			.filter(Objects::nonNull)
			.forEach(broadcastList::add);
		}*/
		return broadcastList;
	}
}
