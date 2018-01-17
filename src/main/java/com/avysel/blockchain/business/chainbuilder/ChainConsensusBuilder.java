package com.avysel.blockchain.business.chainbuilder;

import java.util.List;

import org.apache.log4j.Logger;

import com.avysel.blockchain.business.Blockchain;
import com.avysel.blockchain.business.BlockchainManager;
import com.avysel.blockchain.exception.BlockIntegrityException;
import com.avysel.blockchain.mining.proof.IProof;
import com.avysel.blockchain.mining.proof.ProofOfWork;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.chain.Chain;
import com.avysel.blockchain.model.data.SingleData;

public class ChainConsensusBuilder {

	public enum RejectReason {
		BLOCK_INTEGRITY,
		PROOF_OF_WORK,
		DUPLICATE_BLOCK,
		DUPLICATE_DATA,
		COMPETITION,
		PREVIOUS_HASH,
		PREVIOUS_INDEX,
		OTHER,
		NONE
	};

	private static Logger log = Logger.getLogger("com.avysel.blockchain.business.chainbuilder.ChainConsensusBuilder");

	private Blockchain blockchain;
	private Chain chain;

	public ChainConsensusBuilder(Blockchain blockchain) {
		if(blockchain != null) 
			this.chain = blockchain.getChain();
		this.blockchain = blockchain;
	}

	/**
	 * Check if an incoming block can be added at the end of current chain of instead of an existing block.
	 * @param incomingBlock the block to add
	 * @return true if incoming block has been added or is already in the chain, false if it has been rejected
	 * @throws BlockIntegrityException 
	 */
	public RejectReason processExternalBlock(Block incomingBlock) throws BlockIntegrityException {

		if(incomingBlock == null) return RejectReason.OTHER;

		// integrity of block has to be ok
		if( ! BlockchainManager.checkBlockHash(incomingBlock))	{
			log.warn("Incoming block "+incomingBlock.getHash()+" rejected because of wrong hash.");
			return RejectReason.BLOCK_INTEGRITY;
		}

		// block has to respect proof of work
		IProof proof = new ProofOfWork();
		if(!proof.checkCondition(incomingBlock)) {
			log.warn("Incoming block "+incomingBlock.getHash()+" rejected because it doesn't respect proof of work condition.");
			return RejectReason.PROOF_OF_WORK;
		}

		// block can't already exist in the chain
		Block existingBlock = BlockchainManager.findBlockByHash(chain, incomingBlock.getHash());
		if(existingBlock != null) {
			// The block is already in chain
			log.info("The incoming block is already in chain");
			return RejectReason.DUPLICATE_BLOCK;
		}

		// no one of data in the block can already be in a previous block
		if(dataAlreadyInChain(incomingBlock)) {
			log.warn("Incoming block "+incomingBlock.getHash()+" rejected because it contains data already validated in chain.");
			return RejectReason.DUPLICATE_DATA;
		}

		RejectReason rejectReason = isSuitableNextBlock(incomingBlock);

		if(RejectReason.NONE.equals(rejectReason)) {
			// the incoming block can be easily added at the end of chain
			chain.linkBlock(incomingBlock);
			cleanDataPool(incomingBlock);
			return RejectReason.NONE;
		}
		else {
			log.error("Incoming block cannot be added for some reasons : "+rejectReason);
			return rejectReason;
		}
	}

	private RejectReason isSuitableNextBlock(Block incomingBlock) {
		// can the block be added to the end of chain ?

		Block competitor = findCompetitorInChain(incomingBlock);
		Block lastBlock = chain.getLastBlock();

		if(competitor != null) return RejectReason.COMPETITION;
		if( ! incomingBlock.getPreviousHash().equals(lastBlock.getHash())) return RejectReason.PREVIOUS_HASH;
		if( ! (incomingBlock.getIndex() == incomingBlock.getIndex() +1) ) return RejectReason.PREVIOUS_INDEX;
		return RejectReason.NONE;

	}

	private Block findCompetitorInChain(Block incomingBlock) {
		return BlockchainManager.findBlockByIndex(chain, incomingBlock.getIndex());
	}


	private boolean dataAlreadyInChain(Block incomingBlock) {
		List<SingleData> dataList = incomingBlock.getDataList();
		for(SingleData data : dataList) {
			if(BlockchainManager.findBlockByData(chain, data.getHash()) != null)
				return true;
		}
		return false;
	}

	/**
	 * Remove from blockchain's data pool all given block's data. 
	 * Use it when a block not mined by local node is linked to the chain.
	 * @param block the block that is going to be unlinked from the chain
	 */
	private void cleanDataPool(Block block) {
		// remove from data pool all block data
		blockchain.getDataPool().removeAll(block.getDataList());
	}

	/**
	 * Put back to blockchain's data pool all given block's data. Use it when a block is to be unlinked from the chain.
	 * @param block the block that is going to be unlinked from the chain
	 */
	private void putBackDataToPool(Block block) {
		// put back in pool data of this block (because block is rejected)
		blockchain.getDataPool().addAll(block.getDataList());
	}

	/**
	 * Find out the best block among two blocks. 
	 * The best block means the one that will be kept in the chain when two blocks have the same index 
	 * because created at the same time by two different nodes
	 * @param block1 the first block to compare, will be the best if the two blocks have the same quality
	 * @param block2 the second block to compare, will be rejected if the two blocks have the same quality
	 * @return the block, with the best quality among the two given blocks
	 */
	private Block bestBlock(Block block1, Block block2) {
		// return the best block to keep among the two given blocks

		// TODO verifier aussi la longueur de la chaine suivant le block

		if(block2 == null || block1.getQuality() > block2.getQuality()) {
			return block1;
		}
		else if(block1 == null || block1.getQuality() < block2.getQuality()) {
			return block2;
		}
		else {
			return block1;
		}
	}
}
