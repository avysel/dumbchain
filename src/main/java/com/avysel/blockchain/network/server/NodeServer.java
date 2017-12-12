package com.avysel.blockchain.network.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.avysel.blockchain.network.NetworkManager;

public class NodeServer {

	private static Logger log = Logger.getLogger("com.avysel.blockchain.network.server.NodeServer");
	
	private String host = "127.0.0.1";
	private ServerSocket serverSocket;
	private boolean running = true;
	private NetworkManager network = null;

	public NodeServer(NetworkManager network) {
		this.network = network;
	}

	/**
	 * Create the server socket and start listening network
	 */
	public void start() {
		try {
			serverSocket = new ServerSocket(NetworkManager.getPort(), 100, InetAddress.getByName(host));
			log.info("Create node server for "+host+":"+NetworkManager.getPort());
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		run();
	}

	/**
	 * Start listening network
	 */
	private void run() {

		Thread t = new Thread(new Runnable(){
			public void run(){
				log.info("NodeServer starts runing ...");
				while(running){

					try {
						// wait for client connection
						Socket clientSocket = serverSocket.accept();

						// new thread to process the connection
						log.info("Connexion cliente reçue.");                  
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
		});

		t.start();
	}	

	public void stop() {
		log.info("Stop node server");
		running = false;
	}
}
