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
import com.avysel.blockchain.model.data.ISingleData;

/**
 * Manage incoming blocks and deal with forks
 */
public class ChainConsensusBuilder {

	private static Logger log = Logger.getLogger(ChainConsensusBuilder.class);
	
	// how many blocks can be rejected before starting investigation on chain consistency
	private static final int MAX_CONSECUTIVE_REJECTS_ALLOWED = 5;

	// number of blocks rejected by consensus (not rejects because of bad integrity)
	private int nbConsecutiveRejects;

	// index of last linked block get from network
	private long lastLinkedIndex;

	// is currently checking consistency ?
	private boolean isCheckingConsistency;

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

	private Blockchain blockchain;
	private Chain chain;

	public ChainConsensusBuilder(Blockchain blockchain) {
		if(blockchain != null) 
			this.chain = blockchain.getChain();
		this.blockchain = blockchain;
		this.nbConsecutiveRejects = 0;
		this.isCheckingConsistency = false;
		this.lastLinkedIndex = -10;
	}

	/**
	 * Check if an incoming block can be added at the end of current chain of instead of an existing block.
	 * @param incomingBlock the block to add
	 * @return true if incoming block has been added or is already in the chain, false if it has been rejected
	 * @throws BlockIntegrityException occurs when block integrity is not verified
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
			lastLinkedIndex = incomingBlock.getIndex();
			// remove included data from pool
			cleanDataPool(incomingBlock);
			nbConsecutiveRejects = 0;
			return RejectReason.NONE;
		}
		else {
			log.error("XXXXXXXXXX Incoming block not linked : "+rejectReason);
			nbConsecutiveRejects ++;
			return rejectReason;
		}
	}

	/**
	 * Returns the index of last block got from network and successfully linked. It's considered as the last time the chain was obviously consistent.
	 * @return the index of last block linked from network.
	 */
	public long getLastLinkedIndex() {
		return lastLinkedIndex;
	}

	/**
	 * Set index of the last block got from network and successfully linked
	 * @param lastLinkedIndex the index
	 */
	public void setLastLinkedIndex(long lastLinkedIndex) {
		this.lastLinkedIndex = lastLinkedIndex;
	}

	/**
	 * Check if an incoming block can be linked to the end of the chain. It means that index and hash/previous hash are suitable.
	 * @param incomingBlock the block we want to add to the chain.
	 * @return RejectReason, enum code of the check result.
	 */
	private RejectReason isSuitableNextBlock(Block incomingBlock) {
		// can the block be added to the end of chain ?

		Block competitor = findCompetitorInChain(incomingBlock);
		Block lastBlock = chain.getLastBlock();

		if(competitor != null) return RejectReason.COMPETITION;
		if( ! incomingBlock.getPreviousHash().equals(lastBlock.getHash())) return RejectReason.PREVIOUS_HASH;
		if( ! (incomingBlock.getIndex() == lastBlock.getIndex() +1) ) return RejectReason.PREVIOUS_INDEX;
		return RejectReason.NONE;

	}

	/**
	 * Search if a block with the same index already exists in the chain
	 * @param incomingBlock the block we want to add
	 * @return the block in the chain with same index as incoming block. Null if such a block does not exist.
	 */
	private Block findCompetitorInChain(Block incomingBlock) {
		return BlockchainManager.findBlockByIndex(chain, incomingBlock.getIndex());
	}


	/**
	 * Checks if an incoming blocks contains data that has already been included in a previous block.
	 * @param incomingBlock the block we want to add.
	 * @return true if such data exist, false otherwise.
	 */
	private boolean dataAlreadyInChain(Block incomingBlock) {
		List<ISingleData> dataList = incomingBlock.getDataList();
		for(ISingleData data : dataList) {
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
	 * Check concistency of chain.
	 * If too many blocks are rejected, the chain is probably forking.
	 * Rollback to last accepted block, and catch-up again.
	 */
	public void checkConsistency() {
		
		if(!isCheckingConsistency && nbConsecutiveRejects >= MAX_CONSECUTIVE_REJECTS_ALLOWED) {

			isCheckingConsistency = true;

			log.info("Bad consistency, unlink and catch-up after "+lastLinkedIndex);
			
			
			/*
			 * TODO à remanier
			 * Idee : chaque block est ajouté au chainbuilder (miné et incoming)
			 * A chaque erreur de cohérence détectée (définir : quand a t on une erreur de cohérence), on essaie de lancher le chain builder
			 * Le chain builder doit etre modifié pour essayer de construire la plus grande chaine
			 * Le chaine builder peut ne conserver les blocks que depuis un certain index (définir : comment choisir le block du départ de l'historique
			 * 
			 * Si une plus grande chain est construite, on repart à partir de ça
			 * 
			 */
			

			log.info("Consistency check completed.");
			nbConsecutiveRejects = 0;
			isCheckingConsistency = false;
		}
		
	}
}
