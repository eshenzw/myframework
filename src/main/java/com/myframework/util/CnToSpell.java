package com.myframework.util;

import java.nio.charset.Charset;
import java.util.Date;

public class CnToSpell
{
	/**
	 * 获得单个汉字的Ascii，并用"-"连接成一个字符串
	 * 
	 * @param cn
	 *            char 汉字字符
	 * @return string 错误返回 空字符串,否则返回ascii
	 */
	public static String getCnAscii(char cn)
	{
		byte[] bytes = (String.valueOf(cn)).getBytes(Charset.forName("GBK"));
		if (bytes == null || bytes.length > 2 || bytes.length <= 0)
		{ // 错误
			return "";
		}
		if (bytes.length == 1)
		{ // 英文字符
			return new String(bytes);
		}
		if (bytes.length == 2)
		{ // 中文字符
			int hightByte = 256 + bytes[0];
			int lowByte = 256 + bytes[1];
			String ascii = hightByte + "-" + lowByte;
			return ascii;
		}
		return ""; // 错误
	}

	/**
	 * 返回字符串的全拼,是汉字转化为全拼,其它字符不进行转换
	 * 
	 * @param cnStr
	 *            String字符串
	 * @return String 转换成全拼后的字符串
	 */
	public static String getFullSpell(String cnStr)
	{
		if (null == cnStr || "".equals(cnStr.trim()))
		{
			return cnStr;
		}

		StringBuffer retuBuf = new StringBuffer();
		char[] chars = cnStr.toCharArray();
		for (int i = 0, Len = chars.length; i < Len; i++)
		{
			String ascii = getCnAscii(chars[i]);
			if (ascii.length() == 0)
			{ // 取ascii时出错
				retuBuf.append(chars[i]);
			}
			else
			{
				String spell = CnToSpellResource.getSpellByAscii(ascii);
				if (spell != null)
				{
					// 判断是否多音字
					int t = spell.indexOf(",");
					if (t > 0)
					{
						retuBuf.append(spell.substring(0, t));
					}
					else
					{
						retuBuf.append(spell);
					}

				}
				else
				{
					retuBuf.append(chars[i]);
				}
			}
		}

		return retuBuf.toString();
	}

	/**
	 * 获取汉语字符串的声母组合，每个汉字取拼音的第一个字符组成的一个字符串
	 * 
	 * @param cnStr
	 *            汉字的字符串
	 * @return 每个汉字拼音的第一个字母所组成的汉字
	 */
	public static String getFirstSpell(String cnStr)
	{
		if (null == cnStr || "".equals(cnStr.trim()))
		{
			return cnStr;
		}

		char[] chars = cnStr.toCharArray();
		StringBuffer retuBuf = new StringBuffer();
		for (int i = 0, Len = chars.length; i < Len; i++)
		{
			String ascii = getCnAscii(chars[i]);
			if (ascii.length() == 1)
			{ // 取ascii时出错
				retuBuf.append(chars[i]);
			}
			else
			{
				String spell = CnToSpellResource.getSpellByAscii(ascii);
				if (spell != null && spell.length() > 0)
				{
					retuBuf.append(spell.substring(0, 1));
				}
				else
				{
					retuBuf.append(chars[i]);
				}

			}
		}
		return retuBuf.toString();
	}

	/**
	 * 返回字符串的全拼,是汉字转化为全拼,其它字符不进行转换
	 *
	 * @param cnStr
	 *            String字符串
	 * @return String 转换成全拼后的字符串
	 */
	public static String getCamelSpell(String cnStr)
	{
		if (null == cnStr || "".equals(cnStr.trim()))
		{
			return cnStr;
		}

		StringBuffer retuBuf = new StringBuffer();
		char[] chars = cnStr.toCharArray();
		for (int i = 0, Len = chars.length; i < Len; i++)
		{
			String ascii = getCnAscii(chars[i]);
			if (ascii.length() == 0)
			{ // 取ascii时出错
				retuBuf.append(Character.toUpperCase(chars[i]));
			}
			else
			{
				String spell = CnToSpellResource.getSpellByAscii(ascii);
				if (spell != null)
				{
					// 判断是否多音字
					int t = spell.indexOf(",");
					if (t > 0)
					{
						retuBuf.append(Character.toUpperCase(spell.charAt(0))+spell.substring(1, t));
					}
					else
					{
						retuBuf.append(Character.toUpperCase(spell.charAt(0))+spell.substring(1));
					}

				}
				else
				{
					retuBuf.append(Character.toUpperCase(chars[i]));
				}
			}
		}

		return retuBuf.toString();
	}

	/**
	 * 返回字符串的简拼+全拼,是汉字转化为全拼,其它字符不进行转换
	 * 
	 * @param cnStr
	 *            String字符串
	 * @return String 转换成全拼后的字符串
	 */
	public static String getFirstAndFullSpell(String cnStr)
	{
		if (null == cnStr || "".equals(cnStr.trim()))
		{
			return cnStr;
		}

		StringBuffer firstBuf = new StringBuffer();
		StringBuffer fullBuf = new StringBuffer();
		char[] chars = cnStr.toCharArray();
		for (int i = 0, Len = chars.length; i < Len; i++)
		{
			String ascii = getCnAscii(chars[i]);
			if (ascii.length() == 0)
			{ // 取ascii时出错
				fullBuf.append(chars[i]);
			}
			else
			{
				String spell = CnToSpellResource.getSpellByAscii(ascii);
				if (spell != null && spell.length() > 0)
				{
					firstBuf.append(spell.substring(0, 1));
					// 判断是否多音字
					int t = spell.indexOf(",");
					if (t > 0)
					{
						fullBuf.append(spell.substring(0, t));
					}
					else
					{
						fullBuf.append(spell);
					}

				}
				else
				{
					fullBuf.append(chars[i]);
				}
			}
		}
		fullBuf.append(" ").append(firstBuf);
		return fullBuf.toString();
	}

	public static void main(String[] args)
	{
		Date start = new Date();
		System.out.println(start.getTime());
		String str = null;
		str = "我是赵伟我怕谁good";
		System.out.println("Spell=" + CnToSpell.getFullSpell(str));
		System.out.println("SpellFirst=" + CnToSpell.getFirstSpell(str));
		System.out.println("SpellCamel=" + CnToSpell.getCamelSpell(str));
		System.out.println("FirstAndFullSpell=" + CnToSpell.getFirstAndFullSpell(str));
		Date end = new Date();
		System.out.println(end.getTime());
	}
}
