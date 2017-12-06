package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.avysel.blockchain.network.NetworkManager;

public class UDPTestClient {
	public static void main(String[]args) {
		DatagramSocket clientSocket;
		try {
			clientSocket = new DatagramSocket();
			clientSocket.setBroadcast(true);
			
			String data = "{type=100, data={data=data999, uid=999}}";
			
			// create and send packet
			DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, InetAddress.getByName("127.0.0.1")	, NetworkManager.getPort());
			
			for(int i=0;i<100;i++)
			clientSocket.send(packet);			
			
			
		} catch (SocketException e) {
			
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
