package com.avysel.blockchain.network.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.tools.JsonMapper;

public class NodeServer {

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
			System.out.println("Create node server for "+host+":"+NetworkManager.getPort());
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
				System.out.println("Server starts runing ...");
				while(running){

					try {
						// wait for client connection
						Socket clientSocket = serverSocket.accept();

						// new thread to process the connection
						System.out.println("Connexion cliente reçue.");                  
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
	
	
	
	/**
	 * Create the server socket and start listening network
	 *
	public void createNodeServer(NetworkManager network) {
		try {
			this.network = network;
			datagramSocket = new DatagramSocket(NetworkManager.getPort());
			System.out.println("Create node server for "+host+":"+NetworkManager.getPort());
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		run();
	}

	/**
	 * Start listening network
	 *
	private void run() {

		Thread t = new Thread(new Runnable(){
			public void run(){
				System.out.println("Server starts runing ...");

				try {
					while(running){

						//On s'occupe maintenant de l'objet paquet
						byte[] buffer = new byte[8192];
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

						System.out.println("Wait for packet");
						// wait for data
						datagramSocket.receive(packet);
						System.out.println("Get a packet");


						// read
						String str = new String(packet.getData());
						//System.out.print("Reçu de la part de " + packet.getAddress()	+ " sur le port " + packet.getPort() + " : ");
						System.out.println(str);

						// convert data
						DataBulk bulk = getDataBulk(str);

						// push data to network manager
						network.getIncoming(bulk);

						//On réinitialise la taille du datagramme, pour les futures réceptions
						packet.setLength(buffer.length);

					}
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}					
			}

		});

		t.start();
	}
*/
	private NetworkDataBulk getDataBulk(String data) {
		NetworkDataBulk bulk = JsonMapper.jsonToBulk(data);
		return bulk;
	}

	public void stop() {
		System.out.println("Stop server");
		running = false;
	}
}
