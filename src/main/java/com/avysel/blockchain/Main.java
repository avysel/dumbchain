package com.avysel.blockchain;

import com.avysel.blockchain.business.Chain;
import com.avysel.blockchain.business.ChainManager;

public class Main {
	public static void main (String[] args) {

		Chain chain = new Chain();
		ChainManager manager = new ChainManager(chain);
	}
}
