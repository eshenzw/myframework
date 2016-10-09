package com.myframework.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 封装了一些采用HttpClient发送HTTP请求的方法
 * 
 * @see 本工具所采用的是最新的HttpComponents-Client-4.2.1
 * @see 关于本工具类中的一些解释说明,可参考下方列出的我的三篇文章
 * @see http://blog.csdn.net/jadyer/article/details/7615830
 * @see http://blog.csdn.net/jadyer/article/details/7615880
 * @see http://blog.csdn.net/jadyer/article/details/7802139
 */
public final class HttpUtil
{
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

	/**
	 * OK 200请求成功
	 */
	public static final int SC_OK = 200;
	/**
	 * REDIRECT 303重定向
	 */
	public static final int SC_REDIRECT = 303;
	/**
	 * ERROR 400请求错误
	 */
	public static final int SC_RQUEST_ERROR = 400;
	/**
	 * 401未授权
	 */
	public static final int SC_NOT_ALLOW = 401;
	/**
	 * 403禁止访问
	 */
	public static final int SC_FORBIDDEN = 403;
	/**
	 * 404文件未找到
	 */
	public static final int SC_NOT_FOND = 404;
	/**
	 * 500服务器错误
	 */
	public static final int SC_SERVER_ERROR = 500;
	/**
	 * 请求响应状态
	 */
	public static final String RS_STATUS = "status";
	/**
	 * 请求响应长度
	 */
	public static final String RS_LENGTH = "length";
	/**
	 * 响应内容
	 */
	public static final String RS_CONTENT = "content";
	/**
	 * 默认超时时间
	 */
	public static final int DEFAULT_CONNECT_TIMEOUT = 2 * 1000;
	public static final int DEFAULT_READ_TIMEOUT = 10 * 1000;

	/**
	 * 私有构造函数
	 */
	private HttpUtil()
	{
	}

	/**
	 * 获取请求主体内容 getRequestBody
	 * 
	 * @param is
	 *            请求流对象
	 * @return 请求对象字节数组
	 */
	public static byte[] getStreamBody(InputStream is)
	{
		byte[] body = null;
		ByteArrayOutputStream out = null;
		int c;
		try
		{
			if (is == null)
			{
				return null;
			}
			// 定义输出字节流
			out = new ByteArrayOutputStream();
			// 定义缓冲区大小
			byte[] by = new byte[1024];
			// 从输入字节流中分批读取字节信息
			while ((c = is.read(by)) != -1)
			{
				out.write(by, 0, c);
			}

			body = out.toByteArray();
			if (out != null)
			{
				out.flush();
			}
			// LOGGER.debug("从request中获取的串为：" + new String(body, "utf-8"));
		}
		catch (Exception e)
		{
			LOGGER.warn("获取请求主体信息出错，原因:", e);
		}
		finally
		{
			// 关闭输入输出字节流
			try
			{
				if (out != null)
				{
					out.close();
				}
			}
			catch (IOException e)
			{
				LOGGER.error("关闭输入输出字节流出错，原因:", e);
			}
		}
		return body;
	}

	/**
	 * 发送HTTP_GET请求
	 * 
	 * @see 该方法会自动关闭连接,释放资源
	 * @param requestURL
	 *            请求地址(含参数)
	 * @param decodeCharset
	 *            解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
	 * @return 远程主机响应正文
	 */
	public static Map<String, Object> sendGetRequest(String reqURL, String decodeCharset)
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		long responseLength = 0; // 响应长度
		String responseContent = null; // 响应内容
		HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
		HttpGet httpGet = new HttpGet(reqURL); // 创建org.apache.http.client.methods.HttpGet
		try
		{
			HttpResponse response = httpClient.execute(httpGet); // 执行GET请求
			HttpEntity entity = response.getEntity(); // 获取响应实体
			if (null != entity)
			{
				responseLength = entity.getContentLength();
				responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
				EntityUtils.consume(entity); // Consume response content
			}
			rs.put(RS_STATUS, response.getStatusLine().getStatusCode());
			rs.put(RS_LENGTH, responseLength);
			rs.put(RS_CONTENT, responseContent);
			LOGGER.debug("请求URL：{}", reqURL);
			LOGGER.debug("请求响应：\n{}\n{}", response.getStatusLine(), responseContent);
			return rs;
		}
		catch (ClientProtocolException e)
		{
			LOGGER.error("该异常通常是协议错误导致,比如构造HttpGet对象时传入的协议不对(将'http'写成'htp')或者服务器端返回的内容不符合HTTP协议要求等,堆栈信息如下", e);
		}
		catch (ParseException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
		catch (IOException e)
		{
			LOGGER.error("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下", e);
		}
		finally
		{
			httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
		}
		return null;
	}

	/**
	 * 发送HTTP_POST请求
	 * 
	 * @see 该方法为<code>sendPostRequest(String,String,boolean,String,String)</code>
	 *      的简化方法
	 * @see 该方法在对请求数据的编码和响应数据的解码时,所采用的字符集均为UTF-8
	 * @see 当<code>isEncoder=true</code>时,其会自动对<code>sendData</code>中的[中文][|][
	 *      ]等特殊字符进行<code>URLEncoder.encode(string,"UTF-8")</code>
	 * @param isEncoder
	 *            用于指明请求数据是否需要UTF-8编码,true为需要
	 */
	public static Map<String, Object> sendPostRequest(String reqURL, String sendData, boolean isEncoder)
	{
		return sendPostRequest(reqURL, sendData, isEncoder, null, null);
	}

	/**
	 * 发送HTTP_POST请求
	 * 
	 * @see 该方法会自动关闭连接,释放资源
	 * @see 当<code>isEncoder=true</code>时,其会自动对<code>sendData</code>中的[中文][|][
	 *      ]等特殊字符进行<code>URLEncoder.encode(string,encodeCharset)</code>
	 * @param reqURL
	 *            请求地址
	 * @param sendData
	 *            请求参数,若有多个参数则应拼接成param11=value11¶m22=value22¶m33=value33的形式后,
	 *            传入该参数中
	 * @param isEncoder
	 *            请求数据是否需要encodeCharset编码,true为需要
	 * @param encodeCharset
	 *            编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
	 * @param decodeCharset
	 *            解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
	 * @return 远程主机响应正文
	 */
	public static Map<String, Object> sendPostRequest(String reqURL, String sendData, boolean isEncoder,
			String encodeCharset, String decodeCharset)
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		String responseContent = null;
		long responseLength = 0; // 响应长度
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, DEFAULT_CONNECT_TIMEOUT);
		HttpPost httpPost = new HttpPost(reqURL);
		// httpPost.setHeader(HTTP.CONTENT_TYPE,
		// "application/x-www-form-urlencoded; charset=UTF-8");
		httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
		try
		{
			if (isEncoder)
			{
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for (String str : sendData.split("&"))
				{
					formParams.add(new BasicNameValuePair(str.substring(0, str.indexOf("=")), str.substring(str
							.indexOf("=") + 1)));
				}
				httpPost.setEntity(new StringEntity(URLEncodedUtils.format(formParams, encodeCharset == null ? "UTF-8"
						: encodeCharset)));
			}
			else
			{
				httpPost.setEntity(new StringEntity(sendData));
			}

			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity)
			{
				responseLength = entity.getContentLength();
				responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
				EntityUtils.consume(entity);
			}
			rs.put(RS_STATUS, response.getStatusLine().getStatusCode());
			rs.put(RS_LENGTH, responseLength);
			rs.put(RS_CONTENT, responseContent);
			LOGGER.debug("请求URL：{}", reqURL);
			LOGGER.debug("请求响应：\n{}\n{}", response.getStatusLine(), responseContent);
			return rs;
		}
		catch (Exception e)
		{
			LOGGER.error("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下", e);
		}
		finally
		{
			httpClient.getConnectionManager().shutdown();
		}
		return null;
	}

	/**
	 * 发送HTTP_POST请求
	 * 
	 * @see 该方法会自动关闭连接,释放资源
	 * @see 该方法会自动对<code>params</code>中的[中文][|][ ]等特殊字符进行
	 *      <code>URLEncoder.encode(string,encodeCharset)</code>
	 * @param reqURL
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param encodeCharset
	 *            编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
	 * @param decodeCharset
	 *            解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
	 * @return 远程主机响应正文
	 */
	public static Map<String, Object> sendPostRequest(String reqURL, Map<String, String> params, String encodeCharset,
			String decodeCharset)
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		String responseContent = null;
		long responseLength = 0; // 响应长度
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, DEFAULT_CONNECT_TIMEOUT);
		HttpPost httpPost = new HttpPost(reqURL);
		List<NameValuePair> formParams = new ArrayList<NameValuePair>(); // 创建参数队列
		for (Map.Entry<String, String> entry : params.entrySet())
		{
			formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		try
		{
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset == null ? "UTF-8" : encodeCharset));

			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity)
			{
				responseLength = entity.getContentLength();
				responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
				EntityUtils.consume(entity);
			}
			rs.put(RS_STATUS, response.getStatusLine().getStatusCode());
			rs.put(RS_LENGTH, responseLength);
			rs.put(RS_CONTENT, responseContent);
			LOGGER.debug("请求URL：{}", reqURL);
			LOGGER.debug("请求响应：\n{}\n{}", response.getStatusLine(), responseContent);
			return rs;
		}
		catch (Exception e)
		{
			LOGGER.error("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下", e);
		}
		finally
		{
			httpClient.getConnectionManager().shutdown();
		}
		return null;
	}

	/**
	 * 发送HTTPS_POST请求
	 * 
	 * @see 该方法为<code>sendPostSSLRequest(String,Map<String,String>,String,String)</code>
	 *      方法的简化方法
	 * @see 该方法在对请求数据的编码和响应数据的解码时,所采用的字符集均为UTF-8
	 * @see 该方法会自动对<code>params</code>中的[中文][|][ ]等特殊字符进行
	 *      <code>URLEncoder.encode(string,"UTF-8")</code>
	 */
	public static String sendPostSSLRequest(String reqURL, Map<String, String> params)
	{
		return sendPostSSLRequest(reqURL, params, null, null);
	}

	/**
	 * 发送HTTPS_POST请求
	 * 
	 * @see 该方法会自动关闭连接,释放资源
	 * @see 该方法会自动对<code>params</code>中的[中文][|][ ]等特殊字符进行
	 *      <code>URLEncoder.encode(string,encodeCharset)</code>
	 * @param reqURL
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param encodeCharset
	 *            编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
	 * @param decodeCharset
	 *            解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
	 * @return 远程主机响应正文
	 */
	public static String sendPostSSLRequest(String reqURL, Map<String, String> params, String encodeCharset,
			String decodeCharset)
	{
		String responseContent = "";
		HttpClient httpClient = new DefaultHttpClient();
		X509TrustManager xtm = new X509TrustManager()
		{
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
			{
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
			{
			}

			public X509Certificate[] getAcceptedIssuers()
			{
				return null;
			}
		};
		try
		{
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[]
			{
				xtm
			}, null);
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
			httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

			HttpPost httpPost = new HttpPost(reqURL);
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : params.entrySet())
			{
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset == null ? "UTF-8" : encodeCharset));

			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity)
			{
				responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
				EntityUtils.consume(entity);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("与[" + reqURL + "]通信过程中发生异常,堆栈信息为", e);
		}
		finally
		{
			httpClient.getConnectionManager().shutdown();
		}
		return responseContent;
	}

	/**
	 * 发送HTTP_POST请求
	 * 
	 * @see 若发送的<code>params</code>中含有中文,记得按照双方约定的字符集将中文
	 *      <code>URLEncoder.encode(string,encodeCharset)</code>
	 * @see 本方法默认的连接超时时间为30秒,默认的读取超时时间为30秒
	 * @param reqURL
	 *            请求地址
	 * @param params
	 *            发送到远程主机的正文数据,其数据类型为<code>java.util.Map<String, String></code>
	 * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code><br>
	 *         若通信过程中发生异常则返回"Failed`HTTP状态码",如<code>"Failed`500"</code>
	 */
	public static String sendPostRequestByJava(String reqURL, Map<String, String> params)
	{
		StringBuilder sendData = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet())
		{
			sendData.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		if (sendData.length() > 0)
		{
			sendData.setLength(sendData.length() - 1); // 删除最后一个&符号
		}
		return sendPostRequestByJava(reqURL, sendData.toString());
	}

	/**
	 * 发送HTTP_POST请求
	 * 
	 * @see 若发送的<code>sendData</code>中含有中文,记得按照双方约定的字符集将中文
	 *      <code>URLEncoder.encode(string,encodeCharset)</code>
	 * @see 本方法默认的连接超时时间为30秒,默认的读取超时时间为30秒
	 * @param reqURL
	 *            请求地址
	 * @param sendData
	 *            发送到远程主机的正文数据
	 * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code><br>
	 *         若通信过程中发生异常则返回"Failed`HTTP状态码",如<code>"Failed`500"</code>
	 */
	public static String sendPostRequestByJava(String reqURL, String sendData)
	{
		HttpURLConnection httpURLConnection = null;
		OutputStream out = null; // 写
		InputStream in = null; // 读
		int httpStatusCode = 0; // 远程主机响应的HTTP状态码
		try
		{
			URL sendUrl = new URL(reqURL);
			httpURLConnection = (HttpURLConnection) sendUrl.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoOutput(true); // 指示应用程序要将数据写入URL连接,其值默认为false
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setConnectTimeout(30000); // 30秒连接超时
			httpURLConnection.setReadTimeout(30000); // 30秒读取超时

			out = httpURLConnection.getOutputStream();
			out.write(sendData.toString().getBytes());

			// 清空缓冲区,发送数据
			out.flush();

			// 获取HTTP状态码
			httpStatusCode = httpURLConnection.getResponseCode();

			// 该方法只能获取到[HTTP/1.0 200 OK]中的[OK]
			// 若对方响应的正文放在了返回报文的最后一行,则该方法获取不到正文,而只能获取到[OK],稍显遗憾
			// respData = httpURLConnection.getResponseMessage();

			// //处理返回结果
			// BufferedReader br = new BufferedReader(new
			// InputStreamReader(httpURLConnection.getInputStream()));
			// String row = null;
			// String respData = "";
			// if((row=br.readLine()) != null){
			// //readLine()方法在读到换行[\n]或回车[\r]时,即认为该行已终止
			// respData = row; //HTTP协议POST方式的最后一行数据为正文数据
			// }
			// br.close();

			in = httpURLConnection.getInputStream();
			byte[] byteDatas = new byte[in.available()];
			in.read(byteDatas);
			return new String(byteDatas) + "`" + httpStatusCode;
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			return "Failed`" + httpStatusCode;
		}
		finally
		{
			if (out != null)
			{
				try
				{
					out.close();
				}
				catch (Exception e)
				{
					LOGGER.error("关闭输出流时发生异常,堆栈信息如下", e);
				}
			}
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (Exception e)
				{
					LOGGER.error("关闭输入流时发生异常,堆栈信息如下", e);
				}
			}
			if (httpURLConnection != null)
			{
				httpURLConnection.disconnect();
				httpURLConnection = null;
			}
		}
	}

	/**
	 * Http连接池用于平台Http并发请求
	 */
	private static CloseableHttpClient httpclient = null;
	/**
	 * 虚拟请求cookies防止请求被拒绝
	 */
	private static CookieSpecProvider easySpecProvider = new CookieSpecProvider()
	{
		public CookieSpec create(HttpContext context)
		{

			return new BrowserCompatSpec()
			{
				/*
				 * @Override public void validate(Cookie cookie, CookieOrigin
				 * origin) throws MalformedCookieException { // Oh, I am easy }
				 */
			};
		}
	};

	/**
	 * 通过平台Http连接池发送请求
	 * 
	 * @param url
	 *            请求地址
	 * @param timeout
	 *            超时时间
	 * @return
	 */
	public static String httpClientGet(String url, int timeout)
	{
		if (httpclient == null)
		{
			httpclient = getHttpClientPool(100, 5);
		}

		return httpClientPoolGet(httpclient, url, timeout);
	}

	/**
	 * 获取连接池
	 * 
	 * @param maxTotal
	 *            最大连接数
	 * @param maxPerRoute
	 *            每个路由最大连接数
	 * @return 连接池
	 */
	public static CloseableHttpClient getHttpClientPool(int maxTotal, int maxPerRoute)
	{
		// 解决 ResponseProcessCookies:122 - Cookie rejected: 问题
		Registry<CookieSpecProvider> reg = RegistryBuilder.<CookieSpecProvider> create()
				.register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
				.register(CookieSpecs.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory())
				.register("waiqin365", easySpecProvider).build();
		PoolingHttpClientConnectionManager httpClientPool = new PoolingHttpClientConnectionManager();
		httpClientPool.setMaxTotal(maxTotal);
		httpClientPool.setDefaultMaxPerRoute(maxPerRoute);
		return HttpClients.custom().setConnectionManager(httpClientPool).setDefaultCookieSpecRegistry(reg).build();
	}

	public static String httpClientPoolGet(CloseableHttpClient httpclient, String url, int timeout)
	{

		RequestConfig config = RequestConfig.custom().setCookieSpec("waiqin365").setConnectionRequestTimeout(timeout)
				.setConnectTimeout(timeout).setSocketTimeout(timeout).build();

		try
		{
			HttpGet httpget = new HttpGet(url);
			httpget.setConfig(config);
			// Execute HTTP request
			CloseableHttpResponse response = httpclient.execute(httpget);
			try
			{
				HttpEntity entity = response.getEntity();
				if (entity != null)
				{
					String body = EntityUtils.toString(entity, "UTF-8");
					EntityUtils.consume(entity);
					return body;
				}
			}
			finally
			{
				response.close();
			}
		}
		catch (IOException e)
		{
			LOGGER.warn("请求响应通讯异常：{}", url, e);
		}
		catch (Exception e)
		{
			LOGGER.warn("请求响应异常：{}", url, e);
		}
		return null;
	}

	/**
	 * 通过Http连接池提交POST请求
	 * 
	 * @param httpclient
	 *            http连接池
	 * @param url
	 *            URL地址
	 * @param timeout
	 *            超时时间
	 * @param params
	 *            请求参数
	 * @return
	 */
	public static String httpClientPoolPost(CloseableHttpClient httpclient, String url, int timeout,
			List<BasicNameValuePair> params)
	{

		RequestConfig config = RequestConfig.custom().setCookieSpec("waiqin365").setConnectionRequestTimeout(timeout)
				.setConnectTimeout(timeout).setSocketTimeout(timeout).build();

		try
		{
			HttpPost httpPost = new HttpPost(url);
			// 增加编码头防止Tomcat有Post请求2M大小限制
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
			httpPost.setConfig(config);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			// Execute HTTP request
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try
			{
				HttpEntity entity = response.getEntity();
				if (entity != null)
				{
					String body = EntityUtils.toString(entity, "UTF-8");
					EntityUtils.consume(entity);
					return body;
				}
			}
			finally
			{
				response.close();
			}
		}
		catch (IOException e)
		{
			LOGGER.warn("请求响应通讯异常：{}", url, e);
		}
		catch (Exception e)
		{
			LOGGER.warn("请求响应异常：{}", url, e);
		}
		return null;
	}

	/**
	 * 标准的HttpClient请求
	 * 
	 * @param url
	 *            请求URL
	 * @param timeout
	 *            超时时间
	 * @return 结果
	 */

	public static String httpClientGetSample(String url, int timeout)
	{
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeout).setConnectTimeout(timeout)
				.setSocketTimeout(timeout).build();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try
		{
			HttpGet httpget = new HttpGet(url);
			httpget.setConfig(config);
			// Execute HTTP request
			CloseableHttpResponse response = httpclient.execute(httpget);
			try
			{
				HttpEntity entity = response.getEntity();
				if (entity != null)
				{
					String body = EntityUtils.toString(entity, "UTF-8");
					EntityUtils.consume(entity);
					return body;
				}
			}
			finally
			{
				response.close();
			}
		}
		catch (IOException e)
		{
			LOGGER.warn("请求响应通讯异常：{}", url, e);
		}
		catch (Exception e)
		{
			LOGGER.warn("请求响应异常：{}", url, e);
		}
		finally
		{
			try
			{
				httpclient.close();
			}
			catch (IOException e)
			{
				LOGGER.warn("关闭HTTPCLIENT请求异常：{}", url, e);
			}
		}
		return null;
	}

	/***
	 * 执行http请求
	 * 
	 * @param httpReq
	 * @param isSSL
	 * @return
	 */
	public static String excuteRequest(HttpUriRequest httpReq, boolean isSSL)
	{
		try
		{
			HttpClient httpClient = createClient(isSSL, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
			HttpResponse response = httpClient.execute(httpReq);
			HttpEntity entity = response.getEntity();
			if (entity == null)
			{
				return null;
			}
			String body = EntityUtils.toString(entity, "UTF-8");
			EntityUtils.consume(entity);
			return body;
		}
		catch (Exception e)
		{
			LOGGER.error("http请求异常 ", e);
		}
		return null;
	}

	/**
	 * 创建一个httpclient
	 * 
	 * @param isSSL
	 * @param connectTimeout
	 * @param readTimeout
	 * @return
	 */
	public static HttpClient createClient(boolean isSSL, int connectTimeout, int readTimeout)
	{
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, connectTimeout);
		HttpConnectionParams.setSoTimeout(params, readTimeout);
		HttpConnectionParams.setSocketBufferSize(params, 1024);
		HttpClient httpClient = new DefaultHttpClient(params);
		if (isSSL)
		{
			X509TrustManager xtm = new X509TrustManager()
			{
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
				{
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
				{
				}

				public X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}
			};

			try
			{
				SSLContext ctx = SSLContext.getInstance("TLS");

				ctx.init(null, new TrustManager[]
				{
					xtm
				}, null);

				SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);

				httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

			}
			catch (Exception e)
			{
				LOGGER.error("创建httpclient异常 ", e);
				throw new RuntimeException(e);
			}
		}

		return httpClient;
	}
}