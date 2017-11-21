package com.avysel.blockchain.mining;

import java.util.ArrayList;
import java.util.List;

import com.avysel.blockchain.crypto.HashTools;
import com.avysel.blockchain.model.Block;
import com.avysel.blockchain.model.SingleData;

public class Miner {
	
	public Miner() {
	}

	public Block mine(PendingData pendingData) {
		Block block = new Block();		
		List<SingleData> dataList = new ArrayList<SingleData>();
		
		// TODO quel element a changer pour avoir un nouveau hash a chaque tentative ?
		// si on utilise le timestamp, le check ne fonctionnera pas
		String hash;
		do {
			pendingData.addAll(dataList); // reset working dataset
			block.cleanData();
			
			dataList = pendingData.getRandomData(); // pick new dataset
			for(SingleData data : dataList) { // TODO addAll ?
				block.addData(data);
			}
			
			//System.out.println(pendingData);
			
			hash = HashTools.calculateBlockHash(block);
			System.out.println(hash);
			
			/*
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			 
			 
		} while (!hash.startsWith("0")); // TODO mettre condition en parametre
			
		block.setHash(hash);
		block.setTimestamp(System.currentTimeMillis());
		
		return block;
	}
	
}