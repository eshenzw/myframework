package com.myframework.core.common.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
//import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.apache.http.ssl.SSLContextBuilder;
//import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 *  HTTPClient的工厂类。 可以接收https请求
 *
 * @author  chunhua.zhang
 *  http clients support http
 */
public class HttpClientsFactory implements FactoryBean<CloseableHttpClient>,InitializingBean {
	

	private int connectionRequestTimeout;
	private int connectionTimeout;
	private int socketTimeout;
	
	private CloseableHttpClient closeableHttpClient;
	
	/**
	 * 最大总数
	 */
	private int maxTotal=200;
	
	/**
	 * 默认并发数
	 */
	private int defaultMaxPerRoute=100;
	
	@Override
	public CloseableHttpClient getObject() throws Exception {

		return closeableHttpClient;
	}

	@Override
	public Class<CloseableHttpClient> getObjectType() {
		return CloseableHttpClient.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}


	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	
	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		HttpClientBuilder b = HttpClientBuilder.create();

		// setup a Trust Strategy that allows all certificates.
//      4.4 版本
//		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,
//				new TrustStrategy() {
//					@Override
//					public boolean isTrusted(X509Certificate[] arg0, String arg1)
//							throws CertificateException {
//						return true;
//					}
//				}).build();
		
        SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {//信任所有
                return true;
            }
        }).build();

		b.setSslcontext(sslContext);

//		// don't check Hostnames, either.
//		// -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if
//		// you don't want to weaken
////		HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
//
//		// here's the special part:
//		// -- need to create an SSL Socket Factory, to use our weakened
//		// "trust strategy";
//		// -- and create a Registry, to register it.
//		//
////      4.4 版本		
////		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
////				sslContext, hostnameVerifier);
////		
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext);
		
		
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http",
						PlainConnectionSocketFactory.getSocketFactory())
				.register("https", sslSocketFactory).build();

		// now, we create connection-manager using our Registry.
		// -- allows multi-threaded use
		PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(
				socketFactoryRegistry);
		//总的最大并发连接数
		connMgr.setMaxTotal(maxTotal);
		
		//到每个主机的最大并发连接数
		connMgr.setDefaultMaxPerRoute(defaultMaxPerRoute);
		
		b.setConnectionManager(connMgr);

		//request config
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(connectionRequestTimeout)
				.setConnectTimeout(connectionTimeout)
				.setSocketTimeout(socketTimeout)
				.build();
		b.setDefaultRequestConfig(requestConfig);

		// finally, build the HttpClient;
		// -- done!
		closeableHttpClient = b.build();
	}



}