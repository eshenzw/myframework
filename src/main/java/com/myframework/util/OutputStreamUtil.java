package com.myframework.util;

import java.io.OutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;

import sun.misc.BASE64Decoder;

/**
 * 
 * @author zw<br />
 *         2013-9-29
 */
public class OutputStreamUtil {

	/**
	 * base64加密字符串转为OutputStream
	 * @param base64
	 * @return
	 * @throws Exception
	 */
	public static OutputStream base642OutputStream(String base64) throws Exception {
		OutputStream out = null;
		out = new ByteArrayOutputStream();
		out.write(new BASE64Decoder().decodeBuffer(base64));
		out.flush();
		return out;
	}
}
