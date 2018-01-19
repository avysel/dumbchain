package com.avysel.blockchain.business.chainbuilder;

import java.util.Comparator;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.network.data.message.GetBlocksMessage;
import com.avysel.blockchain.network.peer.Peer;

/**
 * Used by a new node on the network to request other nodes to send the current chain 
 */
public class ChainRequestor {
	
	private Blockchain blockchain;
	
	public ChainRequestor(Blockchain blockchain) {
		super();
		this.blockchain = blockchain;
	}

	private Peer selectPeerToRequest() {
		return blockchain.getPeers().stream().max(Comparator.comparing(Peer::getChainHeight)).get();
	}
	
	public void requestBlocks() {
		Peer peer = selectPeerToRequest();
		GetBlocksMessage message = new GetBlocksMessage();
		message.setStartIndex(blockchain.getLastIndex());
		blockchain.sendMessage(message, peer);
	}
}
