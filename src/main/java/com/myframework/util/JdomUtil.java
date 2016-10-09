/* author hp
 * 创建日期 Feb 25, 2011
 */
package com.myframework.util;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class JdomUtil {

	/**
	 * 获取xml根节点
	 * 
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static Element getRootElement(String xml) throws Exception {
		if (xml == null) {
			return null;
		}
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(xml));
		return doc.getRootElement();
	}
	
	/**
	 * 获取xml根节点
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static Element getRootElement(InputStream in) throws Exception {
		if (in == null) {
			return null;
		}
		SAXBuilder builder = new SAXBuilder();
		return builder.build(in).getRootElement();
	}

	/**
	 * 获取xml根节点
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static Element getRootElement(File file) throws Exception {
		if (file == null) {
			return null;
		}
		SAXBuilder builder = new SAXBuilder();
		return builder.build(file).getRootElement();
	}

	/**
	 * 从element中取数据，先取属性，再取子节点
	 * 
	 * @param element
	 * @param name
	 * @return
	 */
	public static String getValueFromElement(Element element, String name) {
		return getValueFromElement(element, name, null);
	}

	/**
	 * 从element中取数据，先取属性，再取子节点
	 * 
	 * @param element
	 * @param name
	 * @param defaultVFalue
	 * @return
	 */
	public static String getValueFromElement(Element element, String name, String defaultVFalue) {
		if (element == null) {
			return null;
		}
		String result = null;
		result = element.getAttributeValue(name);
		if (result == null) {
			result = element.getChildText(name);
		}
		if (StringUtil.isEmpty(result)) {
			result = defaultVFalue;
		}
		return result;
	}

}
