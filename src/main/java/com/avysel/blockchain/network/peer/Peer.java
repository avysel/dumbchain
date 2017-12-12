package com.avysel.blockchain.network.peer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class Peer {

	private String uid;
	private String ip;
	private int port;
	private ServerSocket server;
	private Socket client;
	private long lastAliveTimestamp;

	public Peer() {
		
	}	
	
	public Peer(String ip, int port) {
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
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
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

	public long getLastAlive() {
		return lastAliveTimestamp;
	}

	public void setLastAlive(long lastAlive) {
		this.lastAliveTimestamp = lastAlive;
	}
	
}
