package com.avysel.blockchain.business.chainbuilder;

import java.util.Comparator;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.network.data.message.CatchUpRequestMessage;
import com.avysel.blockchain.network.peer.Peer;

/**
 * Used by a new node on the network to request other nodes to send the current chain.
 */
public class ChainRequestor {

	private static Logger log = Logger.getLogger(ChainRequestor.class);
	
	/**
	 * The blockchain.
	 */
	private Blockchain blockchain;

	/**
	 * Create a ChainRequestor for the blockchain.
	 * @param blockchain the blockchain to be build by ChainRequestor
	 */
	public ChainRequestor(Blockchain blockchain) {
		super();
		this.blockchain = blockchain;
	}

	/**
	 * Select the peer to use to request the chain.
	 * The peer with the higher chain will be selected.
	 * @return the peer with the higer chain, to request catch-up data
	 */
	private Peer selectPeerToRequest() {
		if(blockchain.getPeers() != null &&  !blockchain.getPeers().isEmpty())
			return blockchain.getPeers().stream().max(Comparator.comparing(Peer::getChainHeight)).get();
		else
			return null;
	}

	/**
	 * Send a request to a peer to catch-up with current shared chain.
	 * @param startIndex index of first requested block
	 */
	public void requestBlocks(long startIndex) {
		Peer peer = selectPeerToRequest();
		if(peer != null) {
			CatchUpRequestMessage message = new CatchUpRequestMessage();
			message.setStartIndex(startIndex);
			log.debug("Send chain request message : "+message);
			blockchain.sendMessage(NetworkDataBulk.MESSAGE_CATCH_UP_REQUEST, message, peer);
		}
	}
}
