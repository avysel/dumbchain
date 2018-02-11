package com.avysel.blockchain.business.chainbuilder;

import java.util.List;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.business.block.Block;
import com.avysel.blockchain.business.block.Genesis;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.network.data.message.CatchUpDataMessage;
import com.avysel.blockchain.network.peer.Peer;

/**
 * Send the current chain to a new node on the network.
 */
public class ChainSender {

	private static Logger log = Logger.getLogger(ChainSender.class);

	/**
	 * The blockchain.
	 */
	private Blockchain blockchain;

	public ChainSender(Blockchain blockchain) {
		super();
		this.blockchain = blockchain;
	}

	/**
	 * Sends the current chain to a peer.
	 * @param peer the requestor peer
	 * @param startIndex index of the first bloc to send
	 */
	public void sendChainToPeer(Peer peer, long startIndex) {
		// no block to send, send an "empty" response message instead
		if(blockchain.getLastIndex() == Genesis.GENESIS_INDEX
			|| 	blockchain.getLastIndex() == startIndex) {
			blockchain.sendMessage(NetworkDataBulk.MESSAGE_CATCH_UP_EMPTY, null, peer);
			log.info("Send empty catch-up response to "+peer);
		} else {
			log.info("Send chain to "+peer);
			
			for (int i = 0; i < blockchain.getChain().getLastIndex(); i++) {
				CatchUpDataMessage message = new CatchUpDataMessage();
				message.setSenderNodeId(this.blockchain.getNodeId());

				// get subchain of MAX_BLOCKS_PER_BULK (or less, if less elements remain) elements
				int from = i * blockchain.getParams().getMaxBlocksInBulk() + (int)startIndex+1;
				
				int to = Math.min(from + blockchain.getParams().getMaxBlocksInBulk()
						, (int)blockchain.getChain().size());
				
				List<Block> sublist = blockchain.getChain().getBlockList().subList(from, to);

				// add the previously selected blocks in the message
				if(sublist != null && !sublist.isEmpty()) {
					message.setBlocks(sublist);
					message.setStartIndex(sublist.get(0).getIndex());
					message.setLastIndex(sublist.get(sublist.size()-1).getIndex());

					// send the group of blocks to the requestor peer
					blockchain.sendMessage(NetworkDataBulk.MESSAGE_CATCH_UP_BLOCKS, message, peer);
					log.info("Send "+sublist.size()+" block(s).");
				}

				// if last sent block is the last block of chain, stop
				if(to == blockchain.getChain().getBlockList().size()) {
					break;
				}
			}
		}
	}
}
