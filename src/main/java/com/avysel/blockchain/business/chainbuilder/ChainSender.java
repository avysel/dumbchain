package com.avysel.blockchain.business.chainbuilder;

import java.util.List;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.block.Genesis;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.network.data.message.CatchUpDataMessage;
import com.avysel.blockchain.network.peer.Peer;

/**
 * Send the current chain to a new node on the network
 */
public class ChainSender {

	private static final int MAX_BLOCKS_PER_BULK = 10;

	private Blockchain blockchain;

	public ChainSender(Blockchain blockchain) {
		super();
		this.blockchain = blockchain;
	}

	public void sendChainToPeer(Peer peer) {

		// no block to send, send an "empty" response message instead
		if(this.blockchain.getLastIndex() == Genesis.GENESIS_INDEX) {

			blockchain.sendMessage(NetworkDataBulk.MESSAGE_CATCH_UP_EMPTY, null, peer);
		}
		else {

			for (int i = 0 ; i < blockchain.getChain().size() ; i += MAX_BLOCKS_PER_BULK) {

				CatchUpDataMessage message = new CatchUpDataMessage();
				List<Block> sublist = blockchain.getChain().getBlockList().subList(i+1, Math.min(MAX_BLOCKS_PER_BULK,blockchain.getChain().getBlockList().size() - i*MAX_BLOCKS_PER_BULK));
				if(sublist != null) {
					message.setBlocks(sublist);
					message.setStartIndex(sublist.get(0).getIndex());
					message.setLastIndex(sublist.get(sublist.size()-1).getIndex());

					blockchain.sendMessage(NetworkDataBulk.MESSAGE_CATCH_UP_BLOCKS, message, peer);
				}
			}
		}
	}

}
