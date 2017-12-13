package com.avysel.blockchain.business.consensus;

import java.util.List;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.chain.Chain;

public class ChainBuilder {

	private static Logger log = Logger.getLogger("com.avysel.blockchain.business.consensus.ChainBuilder");

	private Blockchain blockchain;
	private Chain chain;

	public ChainBuilder(Blockchain blockchain, Chain chain) {
		this.chain = chain;
		this.blockchain = blockchain;
	}

	/**
	 * Check if an incoming block can be added at the end of current chain of instead of an existing block.
	 * @param incomingBlock the block to add
	 * @return true if incoming block has been added, false if it has been rejected
	 */
	public boolean processExternalBlock(Block incomingBlock) {
		// si ajout ok, alors ajout
		// sinon, creation d'une chain part

		if(isSuitableNextBlock(incomingBlock)) {
			// the incoming block can be easily added at the end of chain
			chain.linkBlock(incomingBlock);
			cleanDataPool(incomingBlock);
			return true;
		}
		else {
			// there is a competition between the incoming block and an existing block

			// find out the competitor block in current chain
			Block localCompetitor = findCompetitorInChain(incomingBlock);

			// find out the best block between incoming and local competitor
			Block bestBlock = bestBlock(localCompetitor, incomingBlock);

			if(bestBlock != localCompetitor) { // TODO need an equals ?
				// the best block is the incoming one, remove local block
				List<Block> removedBlocksList = chain.unlinkBlock(localCompetitor);

				// add incoming block
				chain.linkBlock(bestBlock);
				
				// put back in data pool data from removed blocks
				for(Block removedBlock : removedBlocksList) {
					resetDataPool(removedBlock);
				}
				return true;
			}
			else {
				// the best block is the local one, incoming block is rejected, current chain stays unchanged
				return false;
			}
		}
	}

	private boolean isSuitableNextBlock(Block block) {
		// can the block be added to the end of chain ?
		// verifier index, previous ... pour déterminer sa place
		return true;
	}

	private Block findCompetitorInChain(Block block) {
		return new Block();
	}

	private void cleanDataPool(Block block) {
		// remove from data pool all block data
		blockchain.getDataPool().removeAll(block.getDataList());
	}

	private void resetDataPool(Block block) {
		// put back in pool data of this block (because block is rejected)
		blockchain.getDataPool().addAll(block.getDataList());
	}

	private Block bestBlock(Block block1, Block block2) {
		// return the best block to keep among the two given blocks

		// TODO verifier aussi la longueur de la chaine suivant le block

		if(block2 == null || block1.getDifficulty() > block2.getDifficulty()) {
			return block1;
		}
		else if(block1 == null || block1.getDifficulty() < block2.getDifficulty()) {
			return block2;
		}
		else {
			return block1; // TODO random 1 vs 2
		}
	}

}
