package com.myframework.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	private static Log logger = LogFactory.getLog(HtmlUtil.class);
	HttpClient httpClient = null; //HttpClient实例
	GetMethod getMethod =null; //GetMethod实例
	BufferedWriter fw = null;
	String page = null;
	String webappname = null;
	BufferedReader br = null;
	InputStream in = null;
	StringBuffer sb = null;
	String line = null;
	//构造方法
	public HtmlUtil(String webappname){
		this.webappname = webappname;
	}
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

	/** 根据模版及参数产生静态页面 */
	public boolean createHtmlPage(String url,String htmlFileName){
		boolean status = false;
		int statusCode = 0;
		try{
			//创建一个HttpClient实例充当模拟浏览器
			httpClient = new HttpClient();
			//设置httpclient读取内容时使用的字符集
			httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
			//创建GET方法的实例
			getMethod = new GetMethod(url);
			//使用系统提供的默认的恢复策略，在发生异常时候将自动重试3次
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			//设置Get方法提交参数时使用的字符集,以支持中文参数的正常传递
			getMethod.addRequestHeader("Content-Type","text/html;charset=UTF-8");
			//执行Get方法并取得返回状态码，200表示正常，其它代码为异常
			statusCode = httpClient.executeMethod(getMethod);
			if (statusCode!=200) {
				logger.fatal("静态页面引擎在解析"+url+"产生静态页面"+htmlFileName+"时出错!");
			}else{
				//读取解析结果
				sb = new StringBuffer();
				in = getMethod.getResponseBodyAsStream();
				//br = new BufferedReader(new InputStreamReader(in));//此方法默认会乱码，经过长时期的摸索，下面的方法才可以
				br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				while((line=br.readLine())!=null){
					sb.append(line+"\n");
				}
				if(br!=null)br.close();
				page = sb.toString();
				//将页面中的相对路径替换成绝对路径，以确保页面资源正常访问
				page = formatPage(page);
				//将解析结果写入指定的静态HTML文件中，实现静态HTML生成
				writeHtml(htmlFileName,page);
				status = true;
			}
		}catch(Exception ex){
			logger.fatal("静态页面引擎在解析"+url+"产生静态页面"+htmlFileName+"时出错:"+ex.getMessage());
		}finally{
			//释放http连接
			getMethod.releaseConnection();
		}
		return status;
	}

	//将解析结果写入指定的静态HTML文件中
	private synchronized void writeHtml(String htmlFileName,String content) throws Exception{
		File dir = new File(htmlFileName).getParentFile();
		if(!dir.exists()){
			dir.mkdirs();
		}
		fw = new BufferedWriter(new FileWriter(htmlFileName));
		OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(htmlFileName),"UTF-8");
		fw.write(page);
		if(fw!=null)fw.close();
	}

	//将页面中的相对路径替换成绝对路径，以确保页面资源正常访问
	private String formatPage(String page){
		page = page.replaceAll("\\.\\./\\.\\./\\.\\./", webappname+"/");
		page = page.replaceAll("\\.\\./\\.\\./", webappname+"/");
		page = page.replaceAll("\\.\\./", webappname+"/");
		return page;
	}

	/**
	 * 检查字符串中是否有html非法字符
	 *
	 * @param strHtml
	 * @return true 含有非法字符 false 不含有非法字符
	 */
	public static boolean ifHasHtmltag(String strHtml) {
		if (strHtml.contains("<") || strHtml.contains(">")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 替换tag标签为转义字符
	 *
	 * @param strHtml
	 * @return 转义之后的html字符串
	 */
	public static String replaceHtmlTag(String strHtml) {
		String resultHtml = strHtml.replace("<", "&lt;");
		resultHtml = resultHtml.replace(">", "&gt;");
		return resultHtml;
	}

	/**
	 * @author http://hi.baidu.com/vnplalvyulin

	 * @version 1.0
	 *
	 * 按字节长度截取字符串(支持截取带HTML代码样式的字符串)
	 * @param param 将要截取的字符串参数
	 * @param length 截取的字节长度
	 * @param end 字符串末尾补上的字符串
	 * @return 返回截取后的字符串
	 */
	public static String subStringHTML(String param,int length,String end) {
		StringBuffer result = new StringBuffer();
		int n = 0;
		char temp;
		boolean isCode = false; //是不是HTML代码
		boolean isHTML = false; //是不是HTML特殊字符,如&nbsp;
		for (int i = 0; i < param.length(); i++) {
			temp = param.charAt(i);
			if (temp == '<') {
				isCode = true;
			}
			else if (temp == '&') {
				isHTML = true;
			}
			else if (temp == '>' && isCode) {
				n = n - 1;
				isCode = false;
			}
			else if (temp == ';' && isHTML) {
				isHTML = false;
			}

			if (!isCode && !isHTML) {
				n = n + 1;
				//UNICODE码字符占两个字节
				if ( (temp + "").getBytes().length > 1) {
					n = n + 1;
				}
			}

			result.append(temp);
			if (n >= length) {
				break;
			}
		}
		result.append(end);
		//取出截取字符串中的HTML标记
		String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
		//去掉不需要结素标记的HTML标记
		temp_result = temp_result.replaceAll("</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
				"");
		//去掉成对的HTML标记
		temp_result=temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>","$2");
		//用正则表达式取出标记
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
		Matcher m = p.matcher(temp_result);

		List endHTML = new ArrayList();

		while (m.find()) {
			endHTML.add(m.group(1));
		}
		//补全不成对的HTML标记
		for (int i = endHTML.size() - 1; i >= 0; i--) {
			result.append("</");
			result.append(endHTML.get(i));
			result.append(">");
		}

		return result.toString();
	}
}
