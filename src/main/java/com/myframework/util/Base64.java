package com.myframework.util;

import java.io.*;

import org.apache.commons.lang.StringUtils;

/**
 * 加密类
 * 
 * @author Administrator
 */
public class Base64
{
	// 
	// code characters for values 0..63
	// 
	private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
	/**
	 * definded a buffer size
	 */
	private static int BUFSIZE = 16384;
	// 
	// lookup table for converting base64 characters to value in range 0..63
	// 
	private static byte[] codes = new byte[256];
	static
	{
		for (int i = 0; i < 256; i++)
		{
			codes[i] = -1;
		}
		for (int i = 'A'; i <= 'Z'; i++)
		{
			codes[i] = (byte) (i - 'A');
		}
		for (int i = 'a'; i <= 'z'; i++)
		{
			codes[i] = (byte) (26 + i - 'a');
		}
		for (int i = '0'; i <= '9'; i++)
		{
			codes[i] = (byte) (52 + i - '0');
		}
		codes['+'] = 62;
		codes['/'] = 63;
	}

	/**
	 * 加密 byte 数组
	 * 
	 * @param data
	 * @return
	 */
	public static char[] encode(byte[] data)
	{
		// 检验参数
		if (data == null)
		{
			return null;
		}
		char[] out = new char[((data.length + 2) / 3) * 4];
		// 
		// 3 bytes encode to 4 chars. Output is always an even
		// multiple of 4 characters.
		//
		for (int i = 0, index = 0; i < data.length; i += 3, index += 4)
		{
			boolean quad = false;
			boolean trip = false;
			int val = (0xFF & data[i]);
			val <<= 8;
			if ((i + 1) < data.length)
			{
				val |= (0xFF & data[i + 1]);
				trip = true;
			}
			val <<= 8;
			if ((i + 2) < data.length)
			{
				val |= (0xFF & data[i + 2]);
				quad = true;
			}
			out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 1] = alphabet[val & 0x3F];
			val >>= 6;
			out[index + 0] = alphabet[val & 0x3F];
		}
		return out;
	}

	/**
	 * 解密数组
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] decode(char[] data)
	{
		// 检验参数
		if (data == null)
		{
			return null;
		}
		// as our input could contain non-BASE64 data (newlines,
		// whitespace of any sort, whatever) we must first adjust
		// our count of USABLE data so that...
		// (a) we don 't misallocate the output array, and
		// (b) think that we miscalculated our data length
		// just because of extraneous throw-away junk
		int tempLen = data.length;
		for (int ix = 0; ix < data.length; ix++)
		{
			if ((data[ix] > 255) || codes[data[ix]] < 0)
				--tempLen; // ignore non-valid chars and padding
		}
		// calculate required length:
		// -- 3 bytes for every 4 valid base64 chars
		// -- plus 2 bytes if there are 3 extra base64 chars,
		// or plus 1 byte if there are 2 extra.
		int len = (tempLen / 4) * 3;
		if ((tempLen % 4) == 3)
			len += 2;
		if ((tempLen % 4) == 2)
			len += 1;
		byte[] out = new byte[len];
		int shift = 0; // # of excess bits stored in accum
		int accum = 0; // excess bits
		int index = 0;
		// we now go through the entire array (NOT using the 'tempLen ' value)
		for (int ix = 0; ix < data.length; ix++)
		{
			int value = (data[ix] > 255) ? -1 : codes[data[ix]];
			if (value >= 0) // skip over non-code
			{
				accum <<= 6; // bits shift up by 6 each time thru
				shift += 6; // loop, with new bits being put in
				accum |= value; // at the bottom.
				if (shift >= 8) // whenever there are 8 or more shifted in,
				{
					shift -= 8; // write them out (from the top, leaving any
					out[index++] = // excess at the bottom for next iteration.
					(byte) ((accum >> shift) & 0xff);
				}
			}
			// we will also have skipped processing a padding null byte ( '= ')
			// here;
			// these are used ONLY for padding to an even length and do not
			// legally
			// occur as encoded data. for this reason we can ignore the fact
			// that
			// no index++ operation occurs in that special case: the out[] array
			// is
			// initialized to all-zero bytes to start with and that works to our
			// advantage in this combination.
		}
		// if there is STILL something wrong we just have to throw up now!
		if (index != out.length)
		{
			throw new Error("Miscalculated data length (wrote " + index + " instead of " + out.length + ") ");
		}
		return out;
	}

	/**
	 * 加密字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeString(String str)
	{
		// 检验参数
		if (StringUtils.isBlank(str))
		{
			return StringUtils.EMPTY;
		}
		byte[] bt;
		try
		{
			bt = str.getBytes("utf-8");
			char[] cArray = encode(bt);
			return new String(cArray);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 解密字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String decodeString(String str)
	{
		// 检验参数
		if (StringUtils.isBlank(str))
		{
			return StringUtils.EMPTY;
		}
		char[] cArray = str.toCharArray();
		byte[] bt = decode(cArray);
		return (new String(bt));
	}

	/**
	 * 加密文件
	 * 
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public static void encodeFile(String filename) throws FileNotFoundException
	{
		File file = checkFile(filename);
		byte[] decoded = readBytes(file);
		char[] encoded = encode(decoded);
		writeChars(file, encoded);
	}

	/**
	 * 解密文件
	 * 
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public static void decodeFile(String filename) throws FileNotFoundException
	{
		File file = checkFile(filename);
		char[] encoded = readChars(file);
		byte[] decoded = decode(encoded);
		writeBytes(file, decoded);
	}

	/**
	 * 检验文件路径参数和是否存在
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 */
	private static File checkFile(String filename) throws FileNotFoundException
	{
		if (filename == null)
		{
			throw new NullPointerException("file name  is null !");
		}
		File file = new File(filename);
		if (!file.exists())
		{
			throw new FileNotFoundException("file " + filename + " is not found !");
		}
		return file;
	}

	/**
	 * 读文件返回字节数组
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] readBytes(File file)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] rebytes = null;
		InputStream fis = null;
		InputStream is = null;
		try
		{
			fis = new FileInputStream(file);
			is = new BufferedInputStream(fis);
			int count = 0;
			byte[] buf = new byte[16384];
			while ((count = is.read(buf)) != -1)
			{
				if (count > 0)
					baos.write(buf, 0, count);
			}
			rebytes = baos.toByteArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			// 关闭输入输出流
			IOUtils.close(is);
			IOUtils.close(fis);
			IOUtils.close(baos);
		}
		return rebytes;
	}

	/**
	 * 读文件返回字符数组
	 * 
	 * @param file
	 * @return
	 */
	public static char[] readChars(File file)
	{
		CharArrayWriter caw = new CharArrayWriter();
		// 定义一个返回的数组
		char[] reChars = null;
		Reader fr = null;
		Reader in = null;
		try
		{
			fr = new FileReader(file);
			in = new BufferedReader(fr);
			int count = 0;
			char[] buf = new char[BUFSIZE];
			while ((count = in.read(buf)) != -1)
			{
				if (count > 0)
					caw.write(buf, 0, count);
			}
			reChars = caw.toCharArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			// 关闭输入输出流
			IOUtils.close(in);
			IOUtils.close(fr);
			IOUtils.close(caw);
		}
		return reChars;
	}

	/**
	 * 写字节数组到文件
	 * 
	 * @param file
	 * @param data
	 */
	private static void writeBytes(File file, byte[] data)
	{
		OutputStream fos = null;
		OutputStream os = null;
		try
		{
			fos = new FileOutputStream(file);
			os = new BufferedOutputStream(fos);
			os.write(data);
			os.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			// 关闭输入输出流
			IOUtils.close(os);
			IOUtils.close(fos);
		}
	}

	/**
	 * 写字符到文件
	 * 
	 * @param file
	 * @param data
	 */
	private static void writeChars(File file, char[] data)
	{
		Writer fos = null;
		Writer os = null;
		try
		{
			fos = new FileWriter(file);
			os = new BufferedWriter(fos);
			os.write(data);
			os.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			// 关闭输入输出流
			IOUtils.close(os);
			IOUtils.close(fos);
		}
	}
	//
	// public static void main(String[] args)
	// {
	// boolean decode = true;
	// /*
	// * if (args.length == 0) { System.out.println("usage: java Base64
	// [-d[ecode]] filename"); System.exit(0); } for
	// * (int i=0; i<args.length; i++) { if
	// ("-decode".equalsIgnoreCase(args[i])) decode = true; else if
	// * ("-d".equalsIgnoreCase(args[i])) decode = true; }
	// */
	// String filename = "D:\\templateReport.jasper";
	// File file = new File(filename);
	// if (!file.exists())
	// {
	// System.exit(0);
	// }
	// if (decode)
	// {
	// char[] encoded = readChars(file);
	// byte[] decoded = decode(encoded);
	// writeBytes(file, decoded);
	// } else
	// {
	// byte[] decoded = readBytes(file);
	// char[] encoded = encode(decoded);
	// writeChars(file, encoded);
	// }
	// }
}
