package com.avysel.blockchain.network.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.data.NetworkMessageBulk;
import com.avysel.blockchain.network.peer.Peer;

public class PeerListener implements Runnable {

	private DatagramSocket datagramSocket;
	private NetworkManager networkManager;
	private boolean running = true;

	public PeerListener(NetworkManager manager) {
		super();
		this.networkManager = manager;
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}
	
	/**
	 * Start listening network
	 */
	@Override
	public void run() {

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
						NetworkMessageBulk bulk = getMessageBulk(str);

						// push data to network manager
						networkManager.addPeer(getPeer(bulk));
						
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
	
	private NetworkMessageBulk getMessageBulk(String data) {
		
		// convert json to message bulk
		return new NetworkMessageBulk();
		
	}

	private Peer getPeer(NetworkMessageBulk bulk) {
		
		// create peer from received data
		
		return new Peer();
	}
	
	public void stop() {
		running = false;
	}

}
