/*
 * 作者：zw
 * Nov 11, 2009
 */

package com.myframework.extend;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class TCPClient {

	Logger log = Logger.getLogger(this.getClass());

	private String host;
	private int port;
	private Socket socket = null;
	private OutputStream out = null;
	private InputStream in = null;

	public TCPClient(String host, int port, int timeout) {
		this.host = host;
		this.port = port;
		this.setPort(port);
		try {
			this.socket = new Socket();
			this.socket.connect(new InetSocketAddress(InetAddress.getByName(host), this.port), timeout);
			// socket = new Socket(InetAddress.getByName("127.0.0.1"), 8802);
		} catch (UnknownHostException e) {
			log.error("连接到【" + this.host + ":" + this.port + "】失败", e);
			this.close();
		} catch (IOException e) {
			log.error("连接到【" + this.host + ":" + this.port + "】失败", e);
			this.close();
		}
	}

	/**
	 * 向端口发送数据
	 * 
	 * @param content
	 *            发送的数据
	 * @return
	 */
	public String sendTcpInfo(String content) throws Exception {
		byte[] b1 = null;// 发送到服务器的
		byte[] b2 = null;// 服务器返回的
		String result = null;
		try {
			b1 = new byte[1024];
			b1 = content.getBytes();
			this.out = this.socket.getOutputStream();
			this.out.write(b1);

			b2 = new byte[1024];
			this.in = this.socket.getInputStream();
			log.info("available= " + in.available());
			int len = in.read(b2);// 接受服务器消息
			result = new String(b2, 0, len);
		} catch (UnknownHostException e) {
			log.error("发送数据到【" + this.host + ":" + this.port + "】失败", e);
			this.close();
		} catch (IOException e) {
			log.error("发送数据到【" + this.host + ":" + this.port + "】失败", e);
			this.close();
		} finally {
			b1 = null;
			b2 = null;
		}
		return result;
	}

	/**
	 * 关闭连接
	 */
	public void close() {
		try {
			if (this.in != null) {
				this.in.close();
				this.in = null;
			}
			if (this.out != null) {
				this.out.flush();
				this.out.close();
				this.out = null;
			}
			if (this.socket != null) {
				if (!this.socket.isClosed()) {
					this.socket.close();
				}
				this.socket = null;
			}
		} catch (IOException e) {
			log.error("关闭连接失败", e);
		}
	}

	/**
	 * 连接是否关闭
	 * 
	 * @return
	 */
	public boolean isClosed() {
		if (this.socket == null || this.socket.isClosed()) {
			return true;
		} else {
			return false;
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
