package com.avysel.blockchain.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class NodeServer {

	private int port;
	private String host = "127.0.0.1";
	private ServerSocket serverSocket;
	private boolean running = true;
	
	public void createNodeServer() {
		try {
			serverSocket = new ServerSocket(port, 100, InetAddress.getByName(host));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		run();
	}
	
	private void run() {

	      Thread t = new Thread(new Runnable(){
	         public void run(){
	            while(running){
	               
	               try {
	                  //On attend une connexion d'un client
	                  Socket client = serverSocket.accept();
	                  
	                  //Une fois reçue, on la traite dans un thread séparé
	                  System.out.println("Connexion cliente reçue.");                  
	                  Thread t = new Thread(new ClientProcessor(client));
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
	
	public void read(String data) {
		
	}
}
