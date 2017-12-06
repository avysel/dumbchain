package com.avysel.blockchain.mining;

import java.util.ArrayList;
import java.util.List;

import com.avysel.blockchain.crypto.HashTools;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.chain.Chain;
import com.avysel.blockchain.model.data.ISingleData;

public class Miner {
	
	private boolean mining;
	private PendingData pendingData;
	private Chain chain;
	
	public Miner(Chain chain, PendingData pendingData) {
		mining = true;
		this.pendingData = pendingData;
		this.chain = chain;
	}

	public void start() {
		System.out.println("Start miner.");
		while(mining) {
			Block block = mine();
			System.out.println("New block created with "+block.getDataList().size()+" data");
			chain.linkBlock(block);
			System.out.println(block);
			System.out.println("New block linked");
			System.out.println("Remaining : "+pendingData.size());
			if(pendingData.size() < 10) //TODO make miner wait for new data instead of stop
				stop();
			
		}
		System.out.println("End miner.");
		System.out.println("Effort : "+chain.getEffort());		
	}
	
	public void stop() {
		mining = false;
	}
	
	
	public Block mine() {
		Block block = new Block();		
		List<ISingleData> dataList = new ArrayList<ISingleData>();
		
		// TODO quel element a changer pour avoir un nouveau hash a chaque tentative ?
		// si on utilise le timestamp, le check ne fonctionnera pas
		String hash;
		long difficulty = 0;
		do {
			// put unused data back to pending data
			pendingData.addAll(dataList);
			
			// clean current data set
			block.cleanData();
			
			// pick new dataset
			dataList = pendingData.getRandomData();
			block.addAllData(dataList);
			
			//System.out.println(pendingData);
			
			hash = HashTools.calculateBlockHash(block);
			//System.out.println(hash);
			
			/*
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			 
			 difficulty ++;
		} while (! (hash.startsWith("00") || (difficulty > 1000000 && hash.startsWith("0")))); // TODO mettre condition en parametre
			
		block.setHash(hash);
		block.setTimestamp(System.currentTimeMillis());
		block.setDifficulty(difficulty);
		
		return block;
	}
	
}
