package com.avysel.blockchain.network.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.tools.JsonMapper;

/**
 * Broadcast connection request trough network, expected distant peer's answer and create connections
 */
public class PeerExplorer {

	private NetworkManager manager;
	private boolean running = true;

	public PeerExplorer(NetworkManager manager) {
		super();
		this.manager = manager;
	}	


	public void start() {
		/*	Thread t = new Thread(this);
		t.start();*/
		run();
	}

	public void run() {

		try {

			DatagramSocket clientSocket = new DatagramSocket();
			// create broadcast socket
			clientSocket.setBroadcast(true);

			NetworkDataBulk bulk = new NetworkDataBulk();

			String data = JsonMapper.bulkToJson(bulk);

			// create and send packet // TODO listAllBroadcastAddresses
			DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, NetworkManager.getBroadcastAddress(), NetworkManager.getPort());
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

	public void stop() {
		running = false;
	}

	private void explore() {

	}

}
