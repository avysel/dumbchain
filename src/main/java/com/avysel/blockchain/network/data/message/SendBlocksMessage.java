package com.avysel.blockchain.network.data.message;

import java.util.ArrayList;
import java.util.List;

import com.avysel.blockchain.model.block.Block;

public class SendBlocksMessage extends NetworkMessage {
	private long startIndex;
	private long lastIndex;
	private List<Block> blocks;
	
	public SendBlocksMessage() {
		super();
		blocks = new ArrayList<Block>();
	}
	
	public long getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(long startIndex) {
		this.startIndex = startIndex;
	}
	public long getLastIndex() {
		return lastIndex;
	}
	public void setLastIndex(long lastIndex) {
		this.lastIndex = lastIndex;
	}
	public List<Block> getBlocks() {
		return blocks;
	}
	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}
}
