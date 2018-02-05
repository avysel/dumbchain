package com.avysel.blockchain.db;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

import java.io.File;
import java.io.IOException;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.chain.ChainPart;
import com.avysel.blockchain.model.data.ISingleData;
import com.avysel.blockchain.tools.JsonMapper;
import com.avysel.blockchain.tools.Util;

public class DBManager {

	private static String DB_DIR_PATH = "C:\\Developpement\\leveldb";


	public DBManager() {

	}

	private void createDir() {
		File directory = new File(DB_DIR_PATH);
		if(!directory.exists() && !directory.mkdir()) {
			System.out.println("Error while creating DB file");
		}
	}

	public DB openDB() {
		Options options = new Options();
		options.createIfMissing(true);
		DB db = null;

		try {
			createDir();
			db = factory.open(new File(DBManager.DB_DIR_PATH), options);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return db;
	}

	private void closeDB(DB db) {
		try {
			if(db != null)
				db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public void putChain(ChainPart chain) {
		DB db = openDB();
		db.put(Util.bytes(chain.getHash()), Util.bytes(chain));
		closeDB(db);
	}

	public void putBlock(Block block) {
		DB db = openDB();
		db.put(Util.bytes(block.getHash()), Util.bytes(block));
		closeDB(db);
	}

	public void putData(ISingleData data) {
		DB db = openDB();
	}

	public ChainPart getChain() {
		DB db = openDB();
		return null;
	}

	public Block getBlock(String hash) {
		DB db = openDB();
		String sBlock = Util.string(db.get(Util.bytes(hash)));
		return JsonMapper.jsonToBlock(sBlock);
	}

	public ISingleData getData(String uid) {
		DB db = openDB();
		return null;
	}

}
