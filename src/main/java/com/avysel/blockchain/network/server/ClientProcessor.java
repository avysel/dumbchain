package com.avysel.blockchain.network.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.tools.JsonMapper;

/**
 * This class is used to handle a NodeClient connection from another Blockchain instance on the network to the local NodeServer
 *
 */
public class ClientProcessor implements Runnable {

	private static Logger log = Logger.getLogger(ClientProcessor.class);

	private Socket socket;
	private BufferedInputStream reader = null;
	private NetworkManager network = null;

	public ClientProcessor(Socket clientSocket, NetworkManager network){
		socket = clientSocket;
		this.network = network;
	}	

	/**
	 * Creates a thread that process a client connection to the current node's socket
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		log.debug("Start processing incoming client connection");

		String data = new String();
		try {
			reader = new BufferedInputStream(socket.getInputStream());

			InetSocketAddress remote = (InetSocketAddress) socket.getRemoteSocketAddress();

			while(socket != null && !socket.isClosed()) {

				// get data from client socket				
				String tmp = read();
				if(tmp != null && !tmp.isEmpty()) {
					data += tmp;
				}
				else {
					socket.close();
				}

				String debug = "";
				debug = "Thread : " + Thread.currentThread().getName() + ". ";
				debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
				debug += " Sur le port : " + remote.getPort() + ".\n";
				debug += "\t -> Commande reçue : " + data + "\n";
				log.trace("\n" + debug);
			}

			// read the data
			NetworkDataBulk bulk = getDataBulk(data);
			if(bulk != null) {
				// push data to network manager
				network.processIncoming(bulk, remote.getAddress().getHostAddress());
			}		

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads raw data from stream
	 * @return data String that contains raw data read from stream
	 * @throws IOException
	 */
	private String read() {
		String data = "";
		int stream = 0;
		byte[] b = new byte[4096];
		try {
			stream = reader.read(b);
			if(stream != -1 ) {
				data = new String(b, 0, stream);
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(StringIndexOutOfBoundsException e) {
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * Convert raw data from network to NetworkDataBulk object
	 * @param data the string read from network
	 * @return the NetworkDataBulk object created from raw data
	 */
	private NetworkDataBulk getDataBulk(String data) {
		return JsonMapper.jsonToBulk(data);
	}
}