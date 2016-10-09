/*
 * 作者：zw
 * Nov 10, 2009
 */

package com.myframework.extend;

public class SendTCP {

	private static TCPClient client;

	public static void send(String str) {
		String host = "127.0.0.1";
		int port = 8802;

		try {
			if (client == null || client.isClosed()) {
				client = new TCPClient(host, port, 1000);
			}
			System.out.println(client.sendTcpInfo(str));
			// Send.sendTcpInfo(str);
		} catch (Exception e) {
			e.printStackTrace();
			client.close();
			client = null;
		}
	}

}
