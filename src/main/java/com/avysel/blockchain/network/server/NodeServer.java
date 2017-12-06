package com.avysel.blockchain.network.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.avysel.blockchain.network.NetworkManager;

public class NodeServer {

	private String host = "127.0.0.1";
	private ServerSocket serverSocket;
	private boolean running = true;
	private NetworkManager network = null;
	
	/**
	 * Create the server socket and start listening network
	 */
	public void createNodeServer(NetworkManager network) {
		try {
			this.network = network;
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
	                  Socket client = serverSocket.accept();
	                  
	                  // new thread to process the connection
	                  System.out.println("Connexion cliente reçue.");                  
	                  Thread t = new Thread(new ClientProcessor(client, network));
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
}