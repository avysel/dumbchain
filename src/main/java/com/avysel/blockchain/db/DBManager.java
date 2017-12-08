//package com.avysel.blockchain.db;
//import static org.iq80.leveldb.impl.Iq80DBFactory.*;
//
//import java.io.File;
//import java.io.IOException;
//
//import org.iq80.leveldb.DB;
//import org.iq80.leveldb.Options;
//
//public class DBManager {
//
//	private DB db;
//	
//	public void openDB() {
//		Options options = new Options();
//		options.createIfMissing(true);
//		try {
//			db = factory.open(new File("example"), options);
//		} catch (IOException e) {
//			
//			e.printStackTrace();
//		}
//		try {
//		  // Use the db in here....
//		} finally {
//		  // Make sure you close the db to shutdown the 
//		  // database and avoid resource leaks.
//		  try {
//			db.close();
//		} catch (IOException e) {
//		
//			e.printStackTrace();
//		}
//		}		
//	}
//	
//	public void put() {
//		
//	}
//}
