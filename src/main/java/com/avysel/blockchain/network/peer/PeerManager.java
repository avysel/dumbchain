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

	private static Logger log = Logger.getLogger(PeerManager.class);
	private Peer localPeer;
	private PeerExplorer peerExplorer;
	private PeerListener peerListener;

	private List<Peer> peersList = new ArrayList<Peer>();


	public PeerManager(NetworkManager networkManager) {
		super();
		this.peerExplorer = new PeerExplorer(this);
		this.peerListener = new PeerListener(this, networkManager);	
		
	}

	/**
	 * Returns the Peer representing the current node.
	 * @return the local Peer
	 */
	public Peer getLocalPeer() {
		return localPeer;
	}

	/**
	 * Sets the Peer representing the current node.
	 * @param localPeer the current node's Peer representation
	 */
	public void setLocalPeer(Peer localPeer) {
		this.localPeer = localPeer;
	}

	/**
	 * Returns the list of peers to whom the local node is connected.
	 * @return the list of connected peers
	 */
	public List<Peer> getPeersList() {
		synchronized(peersList) {
			return peersList;
		}
	}

	/**
	 * Starts the peer manager. Broadcast a message to the network to notify presence of local node on the network. 
	 * Then start listening to handle any peer's request.
	 */
	public void start() {
		peerExplorer.hello();
		peerListener.start();
	}

	/**
	 * Stop peer manager. Stop listening peer's requests.
	 */
	public void stop() {
		peerListener.stop();
	}

	/**
	 * Add a Peer to the list of connected Peers.
	 * @param peer the Peer to add.
	 */
	public void addPeer(Peer peer) {
		synchronized(peersList) {
			if(!isLocalPeer(peer) && !peerExists(peer)) {
				peersList.add(peer);
				log.info("New peer added : "+peer);
			} else {
				log.debug("Skip adding peer : "+peer);
			}
		}
	}

	/**
	 * Remove a Peer from the list of connected Peers.
	 * @param peer the Peer to remove.
	 */
	public void removePeer(Peer peer) {
		synchronized(peersList) {
			peersList.remove(peer);
		}
	}

	/**
	 * Allows to know if a given Peer is the current local Peer.
	 * @param peer the Peer that is to be compared with local Peer.
	 * @return true if the given Peer is the local Peer.
	 */
	public boolean isLocalPeer(Peer peer) {
		synchronized(localPeer) {
			return this.localPeer.getUid().equals(peer.getUid());
		}
	}	

	/**
	 * Checks if a peer already exists it the local peers list.
	 * @param peer the peer to check.
	 * @return true if peer already exists, false otherwise.
	 */
	public boolean peerExists(Peer peer) {
		synchronized(peersList) {
			return peersList.contains(peer);
		}
	}

	/**
	 * Find a peer by id.
	 * @param peerId the if of wanted peer
	 * @return the peer
	 */
	public Peer findPeer(String peerId) {
		synchronized(peersList) {
			for(Peer peer : peersList) {
				if(peer.getUid().equals(peerId)) {
					return peer;
				}
			}
			return null;
		}
	}

	/**
	 * Store last contact time for the peer.
	 * @param peerId the ID of the peer.
	 */
	public void markPeerAsAlive(String peerId) {
		synchronized(peersList) {
			Peer peer = findPeer(peerId);
			if(peer != null) {
				peer.setLastAliveTimestamp(System.currentTimeMillis());
				log.debug("Peer "+peer.toString()+" is marked as still alive");
			} else {
				log.warn("Peer "+peerId+" is not found to be marked alive");
			}
		}
	}

	/**
	 * Returns the list of peers we had contact with in less than NetworkManager.DEFAULT_PEER_STILL_ALIVE seconds ago.
	 * @return the list of peers considered as still alive
	 */
	public List<Peer> getAlivePeers() {
		synchronized(peersList) {
			List<Peer> alivePeers = new ArrayList<Peer>();
			long currentTime = System.currentTimeMillis();

			for(Peer peer : peersList) {
				if(currentTime - peer.getLastAliveTimestamp() < (NetworkManager.DEFAULT_PEER_STILL_ALIVE * 1000)) {
					alivePeers.add(peer);
				}
			}

			return alivePeers;
		}
	}
}
