package com.myframework.core.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSON;

/**
 * 
 * created by zw
 *
 */
public class HttpClientUtils implements InitializingBean {

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

	private static HttpClientUtils instance;

	private CloseableHttpClient closeableHttpClient;
	

	public CloseableHttpClient getCloseableHttpClient() {
		return closeableHttpClient;
	}

	public void setCloseableHttpClient(CloseableHttpClient closeableHttpClient) {
		this.closeableHttpClient = closeableHttpClient;
	}	
	

	@Override
	public void afterPropertiesSet() throws Exception {

		instance = new HttpClientUtils();
		instance.closeableHttpClient = this.closeableHttpClient;
	}

	/**
	 * post 请求
	 * 
	 * @param url
	 * @param pairs
	 * @param encoding
	 * @return
	 */
	public static String post(String url, List<NameValuePair> pairs, String encoding) {
		HttpPost post = new HttpPost(url.trim());
		try {
			// RequestConfig requestConfig = RequestConfig.custom()
			// .setSocketTimeout(CONNECT_TIMEOUT)
			// .setConnectTimeout(CONNECT_TIMEOUT)
			// .setConnectionRequestTimeout(CONNECT_TIMEOUT)
			// .setExpectContinueEnabled(false).build();
			// post.setConfig(requestConfig);
			// post.addHeader(DEFAULT_HEADER);

			if (pairs != null && pairs.size() > 0) {
				post.setEntity(new UrlEncodedFormEntity(pairs, encoding));
			}

			logger.info("[HttpUtils Post] begin invoke url:{} , params:{}", url, pairs);
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = instance.closeableHttpClient.execute(post);
			long s2 = System.currentTimeMillis() - s1;
			try {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					post.abort();
					logger.error("[HttpUtils Post] error, url : {}  , params : {},  status :{}", url, pairs,
							statusCode);
					return "";
				}

				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						String str = EntityUtils.toString(entity, encoding);
						logger.info(
								"[HttpUtils Post]Debug response, url : {}  , params : {}, response string : {} ,time : {}",
								url, pairs, str, s2);
						return str;
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (Exception e) {
			logger.error("[HttpUtils Post] error, url : {}  , params : {}, response string : {} ,error : {}", url,
					pairs, "", e.getMessage(), e);
		} finally {
			post.releaseConnection();
		}
		return "";
	}

	/**
	 * post 请求
	 * 
	 * @param url
	 * @param pairs
	 * @return
	 */
	public static String post(String url, List<NameValuePair> pairs) {
		HttpPost post = new HttpPost(url.trim());
		try {
			// RequestConfig requestConfig = RequestConfig.custom()
			// .setSocketTimeout(CONNECT_TIMEOUT)
			// .setConnectTimeout(CONNECT_TIMEOUT)
			// .setConnectionRequestTimeout(CONNECT_TIMEOUT)
			// .setExpectContinueEnabled(false).build();
			// post.setConfig(requestConfig);
			// post.addHeader(DEFAULT_HEADER);

			if (pairs != null && pairs.size() > 0) {
				post.setEntity(new UrlEncodedFormEntity(pairs, Consts.UTF_8));
			}

			logger.info("[HttpUtils Post] begin invoke url:{} , params:{}", url, pairs);
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = instance.closeableHttpClient.execute(post);
			long s2 = System.currentTimeMillis();
			try {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					post.abort();
					logger.error("[HttpUtils Post] error, url : {}  , params : {},  status :{}", url, pairs,
							statusCode);
					return "";
				}

				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						String str = EntityUtils.toString(entity, Consts.UTF_8);
						logger.info(
								"[HttpUtils Post]Debug response, url : {}  , params : {}, response string : {} ,time : {}",
								url, pairs, str, s2 - s1);
						return str;
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (Exception e) {
			logger.error("[HttpUtils Post] error, url : {}  , params : {}, response string : {} ,error : {}", url,
					pairs, "", e.getMessage(), e);
		} finally {
			post.releaseConnection();
		}
		return "";
	}

	/**
	 * get 请求
	 * 
	 * @param url
	 * @param pairs
	 * @param encode
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String get(String url, List<NameValuePair> pairs, String encode) {
		String responseString = null;
		// RequestConfig requestConfig = RequestConfig.custom()
		// .setSocketTimeout(CONNECT_TIMEOUT)
		// .setConnectTimeout(CONNECT_TIMEOUT)
		// .setConnectionRequestTimeout(CONNECT_TIMEOUT).build();

		StringBuilder sb = new StringBuilder();
		sb.append(url.trim());
		int i = 0;
		if (pairs != null && pairs.size() > 0) {
			for (NameValuePair entry : pairs) {
				if (i == 0 && !url.contains("?")) {
					sb.append("?");
				} else {
					sb.append("&");
				}
				sb.append(entry.getName());
				sb.append("=");
				String value = entry.getValue();
				try {
					sb.append(URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.warn("encode http get params error, value is {}", value, e);
					sb.append(URLEncoder.encode(value));
				}
				i++;
			}
		}

		logger.info("[HttpUtils Get] begin invoke:" + url.toString());
		HttpGet get = new HttpGet(url.toString());
		// get.setConfig(requestConfig);
		// get.addHeader(DEFAULT_HEADER);

		try {
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = instance.closeableHttpClient.execute(get);
			long s2 = System.currentTimeMillis();
			try {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					get.abort();
					logger.error("[HttpUtils Get] error, url : {}  , params : {},  status :{}", url, pairs, statusCode);
					return "";
				}

				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						responseString = EntityUtils.toString(entity, encode);
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} catch (Exception e) {
				logger.error("[HttpUtils Get]get response error, url:{}", url.toString(), e);
				return responseString;
			} finally {
				if (response != null) {
					response.close();
				}
			}
			logger.info("[HttpUtils Get]Debug url:{} , response string :{},time={}", url.toString(), responseString,
					s2 - s1);
		} catch (Exception e) {
			logger.error("[HttpUtils Get] error, url : {}  , params : {}, response string : {} ,error : {}", url, pairs,
					"", e.getMessage(), e);
		} finally {
			get.releaseConnection();
		}
		return responseString;
	}

	/**
	 * post
	 * 
	 * @param url
	 * @param data
	 * @param encode
	 * @return
	 */
	public static String postJson(String url, JSON data, String encode) {
		String responseString = null;

		logger.info("[HttpUtils PostJson] begin invoke:{},param:{}", url.toString(), data);
		HttpPost post = new HttpPost(url.trim());
		post.setEntity(new StringEntity(data.toJSONString(), ContentType.APPLICATION_JSON));

		// RequestConfig requestConfig = RequestConfig.custom()
		// .setSocketTimeout(CONNECT_TIMEOUT)
		// .setConnectTimeout(CONNECT_TIMEOUT)
		// .setConnectionRequestTimeout(CONNECT_TIMEOUT).build();
		// post.setConfig(requestConfig);
		// post.addHeader(DEFAULT_HEADER);

		try {
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = instance.closeableHttpClient.execute(post);
			long s2 = System.currentTimeMillis();
			try {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					post.abort();
					logger.error("[HttpUtils PostJson] error, url : {}  , params : {},  status :{}", url, data,
							statusCode);
					return "";
				}

				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						responseString = EntityUtils.toString(entity, encode);
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} catch (Exception e) {
				logger.error("[HttpUtils PostJson]post response error, url:{}", url.toString(), e);
				return responseString;
			} finally {
				if (response != null) {
					response.close();
				}
			}
			logger.info("[HttpUtils PostJson]Debug response, url : {}  , params : {}, response string : {} ,time : {}",
					url, data, responseString, s2 - s1);
		} catch (Exception e) {
			logger.error("[HttpUtils PostJson] error, url : {}  , params : {}, response string : {} ,error : {}", url,
					data, "", e.getMessage(), e);
		} finally {
			post.releaseConnection();
		}
		return responseString;
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public static String postJson(String url, JSON data) {
		String responseString = null;

		logger.info("[HttpUtils PostJson] begin invoke:{},param:{}", url.toString(), data);
		HttpPost post = new HttpPost(url.trim());
		post.setEntity(new StringEntity(data.toJSONString(), ContentType.APPLICATION_JSON));

		// RequestConfig requestConfig = RequestConfig.custom()
		// .setSocketTimeout(CONNECT_TIMEOUT)
		// .setConnectTimeout(CONNECT_TIMEOUT)
		// .setConnectionRequestTimeout(CONNECT_TIMEOUT).build();
		// post.setConfig(requestConfig);
		// post.addHeader(DEFAULT_HEADER);

		try {
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = instance.closeableHttpClient.execute(post);
			long s2 = System.currentTimeMillis();
			try {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					post.abort();
					logger.error("[HttpUtils PostJson] error, url : {}  , params : {},  status :{}", url, data,
							statusCode);
					return "";
				}

				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						responseString = EntityUtils.toString(entity, Consts.UTF_8);
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} catch (Exception e) {
				logger.error("[HttpUtils PostJson]post response error, url:{}", url.toString(), e);
				return responseString;
			} finally {
				if (response != null) {
					response.close();
				}
			}
			logger.info("[HttpUtils PostJson]Debug url:{} , response string :{},time={}", url.toString(),
					responseString, s2 - s1);
		} catch (Exception e) {
			logger.error("[HttpUtils PostJson] error, url : {}  , params : {}, response string : {} ,error : {}", url,
					data, "", e.getMessage(), e);
		} finally {
			post.releaseConnection();
		}
		return responseString;
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @param pairs
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String get(String url, List<NameValuePair> pairs) {
		String responseString = null;
		// RequestConfig requestConfig = RequestConfig.custom()
		// .setSocketTimeout(CONNECT_TIMEOUT)
		// .setConnectTimeout(CONNECT_TIMEOUT)
		// .setConnectionRequestTimeout(CONNECT_TIMEOUT).build();

		StringBuilder sb = new StringBuilder();
		sb.append(url.trim());
		int i = 0;
		if (pairs != null && pairs.size() > 0) {
			for (NameValuePair entry : pairs) {
				if (i == 0 && !url.contains("?")) {
					sb.append("?");
				} else {
					sb.append("&");
				}
				sb.append(entry.getName());
				sb.append("=");
				String value = entry.getValue();
				try {
					sb.append(URLEncoder.encode(value, Consts.UTF_8.name()));
				} catch (UnsupportedEncodingException e) {
					logger.warn("encode http get params error, value is {}", value, e);
					sb.append(URLEncoder.encode(value));
				}
				i++;
			}
		}

		logger.info("[HttpUtils Get] begin invoke:{}", url.toString());
		HttpGet get = new HttpGet(url.toString());
		// get.setConfig(requestConfig);
		// get.addHeader(DEFAULT_HEADER);

		try {
			long s1 = System.currentTimeMillis();
			CloseableHttpResponse response = instance.closeableHttpClient.execute(get);
			long s2 = System.currentTimeMillis();
			try {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					get.abort();
					logger.error("[HttpUtils Get] error, url : {}  , params : {},  status :{}", url, pairs, statusCode);
					return "";
				}
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						responseString = EntityUtils.toString(entity, Consts.UTF_8);
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} catch (Exception e) {
				logger.error("[HttpUtils Get]get response error, url:{}", url.toString(), e);
				return responseString;
			} finally {
				if (response != null) {
					response.close();
				}
			}
			logger.info("[HttpUtils Get]Debug url:{} , response string :{},time={}", url.toString(), responseString,
					s2 - s1);
		} catch (Exception e) {
			logger.error("[HttpUtils PostJson] error, url : {}  , params : {}, response string : {} ,error : {}", url,
					pairs, "", e.getMessage(), e);
		} finally {
			get.releaseConnection();
		}
		return responseString;
	}

	/**
	 * HTTPS请求，默认超时为5S
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String connectPostHttps(String url, Map<String, String> params) {

		String responseContent = null;

		HttpPost httpsPost = new HttpPost(url);
		try {
			// RequestConfig requestConfig = RequestConfig.custom()
			// .setSocketTimeout(CONNECT_TIMEOUT)
			// .setConnectTimeout(CONNECT_TIMEOUT)
			// .setConnectionRequestTimeout(CONNECT_TIMEOUT).build();

			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			httpsPost.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));
			// httpsPost.setConfig(requestConfig);
			// httpsPost.addHeader(DEFAULT_HEADER);

			// 绑定到请求 Entry
			for (Map.Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			CloseableHttpResponse response = instance.closeableHttpClient.execute(httpsPost);
			try {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					httpsPost.abort();
					logger.error("[HttpUtils Security] error, url : {}  , params : {},  status :{}", url, params,
							statusCode);
					return "";
				}
				// 执行POST请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity, Consts.UTF_8);
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
			logger.info("requestURI : " + httpsPost.getURI() + ", responseContent: " + responseContent);
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		} finally {
			httpsPost.releaseConnection();
		}
		return responseContent;
	}

}