package com.avysel.blockchain.network.peer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.client.PeerExplorer;
import com.avysel.blockchain.network.server.PeerListener;

/**
 * Manage the list of Peers connected to the current node. It keeps a list of these peers, and maintain it by removing unreachable peers, adding new peers ...
 * It also send message to the network in order to be known by other peers.
 */
public class PeerManager {

	private static Logger log = Logger.getLogger("com.avysel.blockchain.network.peer.PeerManager");
	private Peer localPeer;
	private PeerExplorer peerExplorer;
	private PeerListener peerListener;

	private List<Peer> peersList = new ArrayList<Peer>();


	public PeerManager() {
		super();
		peerExplorer = new PeerExplorer(this);
		peerListener = new PeerListener(this);	
	}

	/**
	 * Returns the Peer representing the current node
	 * @return the local Peer
	 */
	public Peer getLocalPeer() {
		return localPeer;
	}

	/**
	 * Sets the Peer representing the current node
	 * @param localPeer the current node's Peer representation
	 */
	public void setLocalPeer(Peer localPeer) {
		this.localPeer = localPeer;
	}

	public List<Peer> getPeersList() {
		return peersList;
	}

	public void setPeersList(List<Peer> peersList) {
		this.peersList = peersList;
	}

	public void start() {
		peerExplorer.wakeUp();
		peerListener.start();
	}

	public void stop() {
		peerListener.stop();
	}

	/**
	 * Add a Peer to the list of connected Peers
	 * @param peer the Peer to add.
	 */
	public void addPeer(Peer peer) {

		/*	
		 * TODO commented for local test only
		 * if(! isLocalPeer(peer) && ! peerExists(peer)) {
			peersList.add(peer);
			log.info("New peer added : "+peer);
		}
		else {
			log.info("Skip adding peer : "+peer);
		}*/

		peersList.add(peer);
		log.info("New peer added : "+peer);
	}

	/**
	 * Remove a Peer from the list of connected Peers.
	 * @param peer the Peer to remove.
	 */
	public void removePeer(Peer peer) {
		peersList.remove(peer);
	}

	/**
	 * Allows to know if a given Peer is the current local Peer.
	 * @param peer the Peer that is to be compared with local Peer.
	 * @return true if the given Peer is the local Peer.
	 */
	public boolean isLocalPeer(Peer peer) {
		return this.localPeer.getUid().equals(peer.getUid());
	}	

	public boolean peerExists(Peer peer) {
		return peersList.contains(peer);
	}

	/**
	 * Find a peer by IP and port
	 * @param ip the ip
	 * @param port the port
	 * @return the peer that is registered with given ip and port
	 */
	public Peer findPeer(String ip, int port) {
		for(Peer peer : peersList) {
			if(peer.getIp().equals(ip) && peer.getPort() == port) {
				return peer;
			}
		}
		return null;
	}

	/**
	 * Store last contact time for the peer hosted at a given IP/port.
	 * @param ip
	 * @param port
	 */
	public void markPeerAsAlive(String ip, int port) {
		Peer peer = findPeer(ip, port);
		if(peer != null) {
			peer.setLastAlive(System.currentTimeMillis());
			log.info("Peer "+peer.toString()+" is marked as still alive");
		}
		else {
			log.warn("Peer "+ip+"/"+port+" is not found to be marked alive");
		}
	}
	
	/**
	 * Returns the list of peers we had contact with in less than NetworkManager.DEFAULT_PEER_STILL_ALIVE seconds ago.
	 * @return the list of peers considered as still alive
	 */
	public List<Peer> getAlivePeers() {
		List<Peer> alivePeers = new ArrayList<Peer>();
		long currentTime = System.currentTimeMillis();
		
		for(Peer peer : peersList) {
			if(currentTime - peer.getLastAlive() < (NetworkManager.DEFAULT_PEER_STILL_ALIVE * 1000)) {
				alivePeers.add(peer);
			}
		}
		
		return alivePeers;
	}
}
