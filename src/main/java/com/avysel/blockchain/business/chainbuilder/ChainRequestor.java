package com.avysel.blockchain.business.chainbuilder;

import java.util.Comparator;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.network.data.message.CatchUpRequestMessage;
import com.avysel.blockchain.network.peer.Peer;

/**
 * Used by a new node on the network to request other nodes to send the current chain 
 */
public class ChainRequestor {

	private static Logger log = Logger.getLogger(ChainRequestor.class);
	
	private Blockchain blockchain;

	public ChainRequestor(Blockchain blockchain) {
		super();
		this.blockchain = blockchain;
	}

	private Peer selectPeerToRequest() {
		if(blockchain.getPeers() != null &&  ! blockchain.getPeers().isEmpty())
			return blockchain.getPeers().stream().max(Comparator.comparing(Peer::getChainHeight)).get();
		else
			return null;
	}

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
