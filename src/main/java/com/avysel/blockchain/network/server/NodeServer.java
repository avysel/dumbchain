package com.avysel.blockchain.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.peer.Peer;
import com.avysel.blockchain.tools.NetworkTool;

public class NodeServer implements Runnable {

	private static Logger log = Logger.getLogger(NodeServer.class);

	private ServerSocket serverSocket;
	private boolean running = true;
	private NetworkManager network;

	public NodeServer(NetworkManager network) {
		this.network = network;
	}

	/**
	 * Create the server socket and start listening network.
	 */
	public void start() {
		try {
			// create server socket on a random available port
			serverSocket = new ServerSocket(0/*, 100 , InetAddress.getByName(NetworkTool.getLocalIP())*/);

			// store chosen port in current network configuration
			NetworkManager.setServerListeningPort(serverSocket.getLocalPort());

			// creates a Peer that represent the current node, it contains uid, ip and listening port
			network.setLocalPeer(Peer.initFromLocal());
			log.info("Create node server for "+NetworkTool.getLocalIP()+":"+NetworkManager.getServerListeningPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * Start listening network.
	 */
	public void run() {

		log.debug("NodeServer starts runing.");
		while(running){

			try {
				// wait for client connection
				Socket clientSocket = serverSocket.accept();

				// new thread to process the connection
				log.debug("Incoming connection");                  
				Thread t = new Thread(new ClientProcessor(clientSocket, network));
				t.start();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
			serverSocket = null;
		}
	}

	public void stop() {
		log.debug("Stop node server");
		running = false;
	}
}
