package com.avysel.blockchain.db;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

import java.io.File;
import java.io.IOException;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import com.avysel.blockchain.model.block.Block;
import com.avysel.blockchain.model.chain.ChainPart;
import com.avysel.blockchain.model.data.SingleData;
import com.avysel.blockchain.tools.JsonMapper;
import com.avysel.blockchain.tools.Util;

public class DBManager {

	private static String DB_DIR_PATH = "C:\\Developpement\\leveldb";
	
	private DB db;

	private void createDir() {
		File directory = new File(DB_DIR_PATH);
		if(!directory.exists()) {
			directory.mkdir();
		}
	}
	
	public void openDB() {
		Options options = new Options();
		options.createIfMissing(true);
		try {
			createDir();
			db = factory.open(new File(DBManager.DB_DIR_PATH), options);
		} catch (IOException e) {

			e.printStackTrace();
		}
		try {
			// Use the db in here....
			System.out.println(db != null);
		} finally {
			// Make sure you close the db to shutdown the 
			// database and avoid resource leaks.
			try {
				db.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}	
	}

	public void putChain(ChainPart chain) {
		
	}

	public void putBlock(Block block) {
		openDB();
		db.put(Util.bytes(block.getHash()), Util.bytes(block));
	}

	public void putData(SingleData data) {

	}

	public ChainPart getChain() {
		return null;
	}

	public Block getBlock(String hash) {
		openDB();
		String sBlock = Util.string(db.get(Util.bytes(hash)));
		return JsonMapper.jsonToBlock(sBlock);
	}

	public SingleData getData() {
		return null;
	}
	
	
}
