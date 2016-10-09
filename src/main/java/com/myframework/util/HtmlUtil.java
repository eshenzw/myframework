package com.myframework.util;

import java.io.*;
import java.util.List;

import org.apache.xerces.parsers.DOMParser;
import org.cyberneko.html.HTMLConfiguration;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 封装html操作类
 * 
 * @author admin
 * 
 */
public class HtmlUtil
{
	/**
	 * HTML转XML，输出默认utf-8
	 * 
	 * @param html
	 *            代转文档
	 * @throws IOException
	 * @throws SAXException
	 */
	public static Document htmlToXml(String html) throws UnsupportedEncodingException, SAXException, IOException
	{
		return htmlToXml(html.getBytes("utf-8"));
	}

	@Deprecated
	public static String htmlToXmlStr(String html) throws UnsupportedEncodingException, SAXException, IOException
	{
		Document doc = htmlToXml(html.getBytes("utf-8"));
		//
		Writer writer = new StringWriter();
		OutputFormat format = new OutputFormat();
		format.setSuppressDeclaration(true);
		XMLWriter xmlWriter = new XMLWriter(writer, format);
		xmlWriter.write(doc);
		xmlWriter.close();
		return writer.toString();
	}

	/**
	 * HTML转XML，输出默认utf-8
	 * 
	 * @param html
	 *            代转文档
	 * @throws IOException
	 * @throws SAXException
	 */
	public static Document htmlToXml(byte[] html) throws SAXException, IOException
	{
		return htmlToXML(html, "utf-8");
	}

	/**
	 * HTML转XML，输出指定格式
	 * 
	 * @param html
	 *            代转文档
	 * @param charset
	 *            字符集
	 * @throws IOException
	 * @throws SAXException
	 */
	@SuppressWarnings("unchecked")
	public static Document htmlToXML(byte[] html, String encoding) throws SAXException, IOException
	{
		InputSource source = new InputSource(new ByteArrayInputStream(html));

		HTMLConfiguration config = new HTMLConfiguration();
		config.setFeature("http://cyberneko.org/html/features/scanner/cdata-sections", false);
		config.setFeature("http://cyberneko.org/html/features/scanner/script/strip-cdata-delims", true);
		config.setFeature("http://cyberneko.org/html/features/scanner/script/strip-comment-delims", true);
		config.setFeature("http://cyberneko.org/html/features/balance-tags", true);
		config.setFeature("http://xml.org/sax/features/namespaces", true);
		config.setFeature("http://cyberneko.org/html/features/scanner/normalize-attrs", false);
		config.setFeature("http://cyberneko.org/html/features/scanner/ignore-specified-charset", true);

		config.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
		if (encoding != null)
		{
			config.setProperty("http://cyberneko.org/html/properties/default-encoding", encoding);
		}
		DOMParser parser = new DOMParser(config);

		parser.parse(source);

		org.w3c.dom.Document doc = parser.getDocument();
		org.w3c.dom.DocumentType dt = doc.getDoctype();
		if (dt != null)
		{
			doc.removeChild(dt);
		}
		DOMReader reader = new DOMReader();
		Document dom4jDoc = reader.read(doc);

		/* 对SCRIPT标签中的内容加上CDATA * */
		List<org.dom4j.Element> scripts = dom4jDoc.selectNodes("//script");
		for (org.dom4j.Element i : scripts)
		{
			if (i.hasContent() && i.getText().trim().length() > 0)
			{
				String txt = i.getText();
				if (i.getTextTrim().startsWith("<![CDATA[") && i.getTextTrim().endsWith("]]>"))
				{
					txt = txt.substring(txt.indexOf("<![CDATA[") + 9);
					txt = txt.substring(0, txt.lastIndexOf("]]>"));
				}
				if (!i.getTextTrim().startsWith("<![CDATA[") && i.getTextTrim().indexOf("<![CDATA[") > 0)
				{
					continue;
				}
				i.setText("");
				i.addCDATA(txt);
			}
		}

		return dom4jDoc;
	}

	/**
	 * HTML转XML，输出指定格式
	 * 
	 * @param is
	 *            代转流文件
	 * @param charset
	 *            字符集
	 * @throws IOException
	 * @throws SAXException
	 */
	public static Document htmlToXML(InputStream is, String encoding) throws IOException, SAXException
	{
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1)
		{
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();

		return htmlToXML(imgdata, encoding);
	}
}
