package com.myframework.util;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLUtil
{
	private static final Logger logger = LoggerFactory.getLogger(XMLUtil.class);

	/**
	 * 将XML文件转变为document对象
	 * 
	 * @param fileName
	 *            文件名
	 */
	public static Document convertXML2Doc(String fileName)
	{
		Document doc = null;
		try
		{
			SAXReader reader = new SAXReader();
			doc = reader.read(new File(fileName));
		}
		catch (Exception e)
		{
			logger.error("解析xml文件出错。");
		}
		return doc;
	}

	/**
	 * 将XML文件转变为document对象
	 */
	public static Document convertXML2Doc(File file)
	{
		Document doc = null;
		try
		{
			SAXReader reader = new SAXReader();
			doc = reader.read(file);
		}
		catch (Exception e)
		{
			logger.error("解析xml文件出错。");
		}
		return doc;
	}

	/**
	 * 将xml字符串转为Doc对象
	 * 
	 * @param content
	 */
	public static Document convertXMLStr2Doc(String content)
	{
		Document doc = null;
		try
		{
			doc = DocumentHelper.parseText(content);
		}
		catch (Exception e)
		{
			logger.error("解析xml字符串出错。");
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 将java对象转变为XML字符串
	 */
	// public static void convertJava2XML()
	// {
	// }
	/**
	 * 将XML对象转变成树对象
	 */
	// public static void convertXML2Doc()
	// {
	// }
	public static void main(String[] args)
	{
		String content = "<location>" + "<taskid>12345</taskid>" + "<status>0</status>" + "</location>";
		Document doc;
		try
		{
			doc = DocumentHelper.parseText(content);
			Element root = doc.getRootElement();
			for (Iterator<Element> iter = root.elementIterator(); iter.hasNext();)
			{
				Element e = iter.next();
				System.out.println(e.getName() + e.getText());
			}
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
	}
}
