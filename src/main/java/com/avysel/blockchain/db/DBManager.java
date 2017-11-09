package com.avysel.blockchain.db;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;

import java.io.File;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

public class DBManager {

	private DB db;
	
	public void openDB() {
		Options options = new Options();
		options.createIfMissing(true);
		db = factory.open(new File("example"), options);
		try {
		  // Use the db in here....
		} finally {
		  // Make sure you close the db to shutdown the 
		  // database and avoid resource leaks.
		  db.close();
		}		
	}
	
	public void put() {
		
	}
}
