package com.avysel.blockchain.network.peer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.tools.NetworkTool;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Peer {

	private static Logger log = Logger.getLogger(Peer.class);
	
	private String uid;
	private String ip;
	private int port;
	private ServerSocket server;
	private Socket client;
	private long lastAliveTimestamp;
	private long chainHeight;

	public Peer() {
		super();
		this.uid = UUID.randomUUID().toString();
	}	
	
	public Peer(String ip, int port, long chainHeight) {
		super();
		this.ip = ip;
		this.port = port;
		this.chainHeight = chainHeight;
		this.lastAliveTimestamp = System.currentTimeMillis();
	}

	public Peer(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
		this.uid = UUID.randomUUID().toString();
		this.lastAliveTimestamp = System.currentTimeMillis();
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
	
	@JsonIgnore
	public ServerSocket getServer() {
		return server;
	}
	public void setServer(ServerSocket server) {
		this.server = server;
	}
	
	@JsonIgnore
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

	@JsonIgnore
	public long getLastAliveTimestamp() {
		return lastAliveTimestamp;
	}

	public void setLastAliveTimestamp(long lastAlive) {
		this.lastAliveTimestamp = lastAlive;
	}
	
	public long getChainHeight() {
		return chainHeight;
	}

	public void setChainHeight(long chainHeight) {
		this.chainHeight = chainHeight;
	}

	public static Peer initFromLocal() {
		
		Peer peer = new Peer();
		peer.setIp(NetworkTool.getLocalIP());
		peer.setLastAliveTimestamp(System.currentTimeMillis());
		peer.setPort(NetworkManager.getServerListeningPort());
		peer.setUid(UUID.randomUUID().toString());
		
		log.info("Init local peer : "+peer.toString());
		
		return peer;
	}
	
	public String toString() {
		return this.ip+":"+this.port;
	}
	
	public boolean equals(Peer otherPeer) {
		return this.uid.equals(otherPeer.getUid());
	}
}
