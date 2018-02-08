package com.avysel.blockchain.db;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

import java.io.File;
import java.io.IOException;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBException;
import org.iq80.leveldb.Options;

import com.avysel.blockchain.business.chain.Chain;
import com.avysel.blockchain.tools.JsonMapper;
import com.avysel.blockchain.tools.Util;

public class DBManager {

	private static String DB_DIR_PATH = "C:\\Developpement\\leveldb";

	private static String BLOCKCHAIN_ID_FIELD_KEY = "localNodeId";

	private DB db;

	public DBManager() {
		db = null;
	}

	private void createDir() {
		File directory = new File(DB_DIR_PATH);
		if(!directory.exists() && !directory.mkdir()) {
			System.out.println("Error while creating DB file");
		}
	}

	public void openDB() {
		if(db != null) return;
		Options options = new Options();
		options.createIfMissing(true);
		try {
			createDir();
			db = factory.open(new File(DBManager.DB_DIR_PATH), options);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void closeDB() {
		try {
			if(db != null)
				db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getStoredNodeId() {
		openDB();

		byte[] nodeId = db.get(Util.bytes(BLOCKCHAIN_ID_FIELD_KEY));
		closeDB();

		return Util.string(nodeId);
	}

	public void putChain(String nodeId, Chain chain) {
		openDB();
		
		byte[] nodeIdBytes = Util.bytes(nodeId);
		byte[] chainBytes = Util.bytes(chain);
		try {
		db.put(nodeIdBytes, chainBytes);
		} catch(DBException dbe) {
			dbe.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		closeDB();
	}

	public Chain getChain(String nodeId) {
		openDB();
		if(nodeId != null) {
			String jsonData = Util.string(db.get(Util.bytes(nodeId)));
			Chain chain = JsonMapper.jsonToChain(jsonData);
			closeDB();
			return chain;
		}
		closeDB();
		return null;
	}	
	
	/*

	public void putBlock(Block block) {
		openDB();
		db.put(Util.bytes(block.getHash()), Util.bytes(block));
		closeDB();
	}

	public void putData(ISingleData data) {
		openDB();
	}

	public Block getBlock(String hash) {
		openDB();
		String sBlock = Util.string(db.get(Util.bytes(hash)));
		closeDB();
		return JsonMapper.jsonToBlock(sBlock);
	}

	public ISingleData getData(String uid) {
		openDB();
		return null;
	}
*/
}
