package com.avysel.blockchain.network.peer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
	private NetworkManager networkManager;
	private PeerExplorer peerExplorer;
	private PeerListener peerListener;

	private List<Peer> peersList = new ArrayList<Peer>();

	
	public PeerManager(NetworkManager networkManager) {
		super();
		this.networkManager = networkManager;
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
	 * Sets the Peer represening the current node
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
		startPeerCleaner();
	}
	
	public void stop() {
		peerListener.stop();
		stopPeerCleaner();
	}
	
	/**
	 * Add a Peer to the list of connected Peers
	 * @param peer the Peer to add.
	 */
	public void addPeer(Peer peer) {

	/*	if(! isLocalPeer(peer) && ! peerExists(peer)) {
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

	private void stopPeerCleaner() {
		cleanPeersList();
	}	
	
	private void startPeerCleaner() {
		cleanPeersList();
	}
	
	/**
	 * Checks if all connected Peers are stil alive, remove Peers that did not respond for more than DEFAULT_PEER_STILL_ALIVE seconds.
	 */
	private void cleanPeersList() {
		for(Peer peer : peersList) {
			try {
				InetAddress addr = InetAddress.getByName(peer.getIp());
				if(!addr.isReachable(5000)) {
					removePeer(peer);
					log.warn("Removing unreachable peer "+peer);
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
