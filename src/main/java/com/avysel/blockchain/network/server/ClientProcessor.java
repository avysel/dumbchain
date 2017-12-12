package com.avysel.blockchain.network.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.avysel.blockchain.network.NetworkManager;
import com.avysel.blockchain.network.data.NetworkDataBulk;
import com.avysel.blockchain.tools.JsonMapper;

/**
 * This class is used to handle a @NodeClient connection from another @Blockchain instance on the network to the local @NodeServer
 *
 */
public class ClientProcessor implements Runnable {

	private static Logger log = Logger.getLogger("com.avysel.blockchain.network.server.ClientProcessor");
	
	private Socket socket;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;
	private NetworkManager network = null;

	public ClientProcessor(Socket clientSocket, NetworkManager network){
		socket = clientSocket;
		this.network = network;
	}	

	@Override
	public void run() {
		log.info("Start processing incoming client connection");

		while(socket != null && !socket.isClosed()) {
			try {
				writer = new PrintWriter(socket.getOutputStream());
				reader = new BufferedInputStream(socket.getInputStream());

				// get data from client socket				
				String data = read();

				InetSocketAddress remote = (InetSocketAddress) socket.getRemoteSocketAddress();

				String debug = "";
				debug = "Thread : " + Thread.currentThread().getName() + ". ";
				debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
				debug += " Sur le port : " + remote.getPort() + ".\n";
				debug += "\t -> Commande reçue : " + data + "\n";
				log.trace("\n" + debug);				

				// read the data
				NetworkDataBulk bulk = getDataBulk(data);

				// push data to network manager
				network.getIncoming(bulk);

				// send response data to client
				String response = "OK";

				writer.write(response);
				writer.flush();


			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reads raw data from stream
	 * @return data
	 * @throws IOException
	 */
	private String read() throws IOException{
		String data = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		data = new String(b, 0, stream);
		return data;
	}

	private NetworkDataBulk getDataBulk(String data) {
		return JsonMapper.jsonToBulk(data);
	}
}