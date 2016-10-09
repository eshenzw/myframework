package com.myframework.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 
 * @author zw
 */

public class DownloadUtil {

	public static void download(String url, String filePathName) {
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpGet httpget = new HttpGet(url);
		
			//伪装成google的爬虫JAVA问题查询
			httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
			// Execute HTTP request
			//System.out.println("download " + httpget.getURI());
			HttpResponse response = httpclient.execute(httpget);
		
			File storeFile = new File(filePathName);
			// 目录已存在创建文件夹  
	        if (!storeFile.getParentFile().exists()) { 
	        	//System.out.println("create dir " + filePathName);
	        	storeFile.getParentFile().mkdirs();// 目录不存在的情况下，会抛出异常  
	        }  
	        try {
	        	storeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
			FileOutputStream output = new FileOutputStream(storeFile);
		
			// 得到网络资源的字节数组,并写入文件
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					byte b[] = new byte[1024];
					int j = 0;
					while( (j = instream.read(b))!=-1){
						output.write(b,0,j);
						}
						output.flush();
						output.close();
				} catch (IOException ex) {
					// In case of an IOException the connection will be released
					// back to the connection manager automatically
					throw ex;
				} catch (RuntimeException ex) {
					// In case of an unexpected exception you may want to abort
					// the HTTP request in order to shut down the underlying
					// connection immediately.
					httpget.abort();
					throw ex;
				} finally {
					// Closing the input stream will trigger connection release
					try { 
						instream.close(); 
					} catch (Exception ignore) {}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

	public static void main(String[] args) throws MalformedURLException {
		//抓取下面图片的测试
		DownloadUtil.download("http://image.hunjuwang.com/201409/e889d630-d276-4a68-bca3-e34ab7e9e9dc.jpeg", "F:/aaa/bbb.jpg");
	}
}
