package com.avysel.blockchain.tools;

import java.net.DatagramSocket;

public class NetworkTool {

	public static int getNextAvailablePort(int startPort, int maxTries) {
		for(int p = startPort ; p < startPort + maxTries ; p++) {
			if(!isPortInUse(p)) {
				return p;
			}
		}

		return -1;
	}

	private static boolean isPortInUse(int portNumber) {
		try {
			DatagramSocket s = new DatagramSocket(portNumber);
			s.close();
			return false;
		}
		catch(Exception e) {
			return true;
		}
	}
}
