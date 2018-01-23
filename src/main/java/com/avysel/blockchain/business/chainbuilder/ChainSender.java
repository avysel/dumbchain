package com.avysel.blockchain.business.chainbuilder;

import java.util.List;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.block.Genesis;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.network.data.message.CatchUpDataMessage;
import com.avysel.blockchain.network.peer.Peer;
import com.oracle.tools.packager.Log;

/**
 * Send the current chain to a new node on the network
 */
public class ChainSender {

	private static Logger log = Logger.getLogger("com.avysel.blockchain.business.chainbuilder.ChainSender");
	
	private static final int MAX_BLOCKS_PER_BULK = 1;

	private Blockchain blockchain;

	public ChainSender(Blockchain blockchain) {
		super();
		this.blockchain = blockchain;
	}

	public void sendChainToPeer(Peer peer) {

		// no block to send, send an "empty" response message instead
		if(this.blockchain.getLastIndex() == Genesis.GENESIS_INDEX) {
			blockchain.sendMessage(NetworkDataBulk.MESSAGE_CATCH_UP_EMPTY, null, peer);
			log.debug("Send empty catch-up response to "+peer);
		}
		else {
			log.info("Send chain to "+peer);
			for (int i = 0 ; i < blockchain.getChain().size() ; i += MAX_BLOCKS_PER_BULK) {

				CatchUpDataMessage message = new CatchUpDataMessage();
				List<Block> sublist = blockchain.getChain().getBlockList().subList(
							i * MAX_BLOCKS_PER_BULK +1, 
							Math.min( (i+1)*MAX_BLOCKS_PER_BULK +1, blockchain.getChain().getBlockList().size())
						);
				if(sublist != null && !sublist.isEmpty()) {
					message.setBlocks(sublist);
					message.setStartIndex(sublist.get(0).getIndex());
					message.setLastIndex(sublist.get(sublist.size()-1).getIndex());

					blockchain.sendMessage(NetworkDataBulk.MESSAGE_CATCH_UP_BLOCKS, message, peer);
				}
			}
		}
	}

}
