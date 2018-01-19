package com.avysel.blockchain.test.network;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import com.avysel.blockchain.network.peer.Peer;

public class PeerTest {
	@Test
	public void selectPeer() {
		Peer peer1 = new Peer("11", 21, 256);
		Peer peer2 = new Peer("11", 21, 2);
		Peer peer3 = new Peer("11", 21, 25000);
		Peer peer4 = new Peer("11", 21, 695);
		List<Peer> list = Arrays.asList(peer1, peer2, peer3, peer4);
		
		Peer selected = list.stream().max(Comparator.comparing(Peer::getChainHeight)).get();
		System.out.println(selected.getChainHeight());
	}
}
