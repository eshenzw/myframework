/*
 * 作者：zw
 * Nov 10, 2011
 */

package com.myframework.extend;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.myframework.util.StringUtil;

public abstract class BaseTCPServer extends Thread {

	Logger log = Logger.getLogger(this.getClass());

	protected int port;
	protected String charset;

	private OutputStream out;
	private InputStream in;

	private ServerSocket ss = null;

	public BaseTCPServer() {

	}

	public BaseTCPServer(int port) {
		this.port = port;
	}

	public BaseTCPServer(int port, String charset) {
		this.port = port;
		this.charset = charset;
	}

	public void init() {
		try {
			log.info("开始监听端口：" + this.port);
			ss = new ServerSocket(this.port);
			this.start();
		} catch (Exception e) {
			log.error("监听端口 " + this.port + " 失败！", e);
		}
	}

	public void run() {
		while (true) {
			Socket s = null;
			try {
				s = ss.accept();
				out = s.getOutputStream();
				in = s.getInputStream();
				int len = 0;
				byte[] b = new byte[4096];
				String receiveData = null;
				if ((len = in.read(b)) != -1) {
					if (StringUtil.isEmpty(charset)) {
						receiveData = new String(b, 0, len);
					} else {
						receiveData = new String(b, 0, len, charset);
					}
				}
				this.returnData(process(receiveData.toString()));
			} catch (Exception e) {
				log.error("监听端口 " + this.port + " 失败！", e);
			} finally {
				try {
					this.out.flush();
					this.out.close();
					this.in.close();
					// s.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public abstract String process(Object obj) throws Exception;

	/**
	 * 返回数据
	 * 
	 * @param str
	 */
	protected void returnData(String str) {
		if (StringUtil.isEmpty(str)) {
			return;
		}
		if (this.out != null) {
			try {
				out.write(str.getBytes());
			} catch (IOException e) {
				log.error("返回数据失败！", e);
			}
		}
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}
