package com.avysel.blockchain.mining;

import java.util.ArrayList;
import java.util.List;

import com.avysel.blockchain.crypto.HashTools;
import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.data.ISingleData;

public class Miner {
	
	public Miner() {
	}

	public Block mine(PendingData pendingData) {
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
