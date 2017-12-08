package com.avysel.blockchain.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class Peer {

	private String uid;
	private String ip;
	private String port;
	private ServerSocket server;
	private Socket client;

	public Peer() {
		
	}	
	
	public Peer(String ip, String port) {
		super();
		this.ip = ip;
		this.port = port;
		this.uid = UUID.randomUUID().toString();
	}
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public ServerSocket getServer() {
		return server;
	}
	public void setServer(ServerSocket server) {
		this.server = server;
	}
	public Socket getClient() {
		return client;
	}
	public void setClient(Socket client) {
		this.client = client;
	}
	@Override
	public boolean equals(Object peer) {
		return this.uid.equals(((Peer) peer).getUid());
	}
}
