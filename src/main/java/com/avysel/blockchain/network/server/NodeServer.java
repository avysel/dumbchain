package com.avysel.blockchain.network.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.avysel.blockchain.network.DataBulk;
import com.avysel.blockchain.network.NetworkManager;

public class NodeServer {

	private String host = "127.0.0.1";
	private DatagramSocket datagramSocket;
	private boolean running = true;
	private NetworkManager network = null;

	/**
	 * Create the server socket and start listening network
	 */
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
	 */
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

	private DataBulk getDataBulk(String data) {
		DataBulk bulk = new DataBulk();
		// TODO parse json, set Type
		bulk.setType(DataBulk.DATATYPE_DATA);
		bulk.setData(data);
		return bulk;
	}
}
