package com.myframework.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import sun.misc.BASE64Encoder;

/**
 * 
 * @author zw<br />
 * 2013-4-11
 */
public class InputStreamUtil {
		
	/**
	 * InputStream转为String
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String inputStreamToString(InputStream in) throws Exception {
		return inputStreamToString(in, 2048);
	}
	
	/**
	 * InputStream转为String
	 * @param in
	 * @param length
	 * @return
	 */
	public static String inputStreamToString(InputStream in, int length) throws Exception {
		Reader read = null;
		StringBuffer sb = new StringBuffer();
		try {
			byte[] b = new byte[length];
			int i = 0;
			read = new InputStreamReader(in);
			while ((i = in.read(b)) != -1) {
				if (i != length) {
					sb.append(new String(b, 0, i));
				} else {
					sb.append(new String(b));
				}
			}
		} finally {
			if (read != null) {
				read.close();
			}
		}
		return sb.toString();
	}
	
	/**
	 * string转为InputStream
	 * @param str
	 * @return
	 */
	public static InputStream stringToInputStream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}
	
	/**
	 * 文件流转为base64加密字符串
	 * @param in
	 * @return
	 */
	public static String inputStream2Base64(InputStream in) throws Exception {
		StringBuilder sb = new StringBuilder();
		int BUFFER_LENGTH = 1024 * 20;
		try {
			byte[] b = new byte[BUFFER_LENGTH];
			int l = 0;
			byte[] a = null;
			while ((l = in.read(b)) != -1) {
				if (l != BUFFER_LENGTH) {
					a = new byte[l];
					System.arraycopy(b, 0, a, 0, l);
					sb.append(new BASE64Encoder().encode(a));
				} else {
					sb.append(new BASE64Encoder().encode(b));
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return sb.toString();
	}
	
}
