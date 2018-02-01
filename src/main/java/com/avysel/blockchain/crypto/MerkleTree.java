package com.avysel.blockchain.crypto;

import java.util.ArrayList;

import com.avysel.blockchain.model.block.Block;

public class MerkleTree {


	public boolean checkTree(/* ? */) {
		return true;
	}



	public static String computeMerkleRoot(Block block) {
		return computeMerkleRoot(block.getDataHashesList());
	}

	/**
	 * Compute the merkle root for a list of hashes
	 * @param hashes the list of hashes to "merkleize"
	 * @return the merkle hash for the list of hashes
	 */
	public static String computeMerkleRoot(ArrayList<String> hashes) {

		if(hashes == null || hashes.isEmpty())
			return null;
		if(hashes.size() == 1)
			return hashes.get(0);

		while(hashes.size() > 1) {
			try {
				// if odd number of items, duplicate last item
				if(hashes.size() % 2 != 0) {
					hashes.add(hashes.get(hashes.size()-1));
				}

				// concatenate and hash the hashes 2 at a time
				ArrayList<String> tmpHashes = new ArrayList<String>();
				for(int i = 0;i < hashes.size();i+=2) {
					String hash1 = hashes.get(i);
					String hash2 = hashes.get(i+1);
					String concatenatedHashes = hash1.concat(hash2);
					String digest = HashTools.calculateHash(concatenatedHashes.getBytes());
					tmpHashes.add(digest);
				}

				// the list of new hashes is saved to be processed again
				hashes = tmpHashes;
			}
			catch(NullPointerException e) {
				e.printStackTrace();
			}
		}

		// the last hash in list is the "super hash" of all hashes
		return hashes.get(0);
	}
}
