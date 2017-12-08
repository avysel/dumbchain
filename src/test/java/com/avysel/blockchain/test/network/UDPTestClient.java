package com.avysel.blockchain.test.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.UUID;

import com.avysel.blockchain.network.NetworkManager;

/**
 * Simulate the network that send some new data to the other nodes
 *
 */
public class UDPTestClient {
	public static void main(String[]args) {
		DatagramSocket clientSocket;
		try {

			while(true) {

				// send a random number of data (up to 100), and wait a random number of seconds (up to 10)
				
				int seconds = (new Random()).nextInt(10);
				int number = (new Random()).nextInt(100);

				clientSocket = new DatagramSocket();
				clientSocket.setBroadcast(true);

				for(int i=0;i<number;i++) {
					String randomData = UUID.randomUUID().toString().substring(0, 6);
					String data = "{type=100, data={data=data"+randomData+", uid="+randomData+"}}";
					System.out.println("Send data : "+data);
					// create and send packet
					DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, InetAddress.getByName("127.0.0.1")	, NetworkManager.getPort());
					clientSocket.send(packet);		
				}
				Thread.sleep(seconds * 1000);

			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
