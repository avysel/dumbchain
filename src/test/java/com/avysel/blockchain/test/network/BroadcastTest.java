package com.avysel.blockchain.test.network;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

import org.junit.Test;

import com.avysel.blockchain.tools.NetworkTool;

public class BroadcastTest {

	@Test
	public void broadcastAddress() {

		try {
			List<InetAddress> addresses = NetworkTool.listAllBroadcastAddresses();
			
			for(InetAddress addr : addresses) {
				System.out.print(addr + " - ");
			}
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	
	}
}
