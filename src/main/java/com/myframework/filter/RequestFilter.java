package com.myframework.filter;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Request Filter 作用是将request,response设置到ThreadLocal中。
 * 同一个request请求处理线程可以通过getRequestLocal(),getResponseLocal()
 * 获得当前的request和response值。
 * 
 * @author <a href="mailto:fanwenqiang@nj.fiberhome.com.cn">AndyFan*</a>
 */
public class RequestFilter implements Filter
{
	/***/
	private static transient Log log = LogFactory.getLog(RequestFilter.class);
	/***/
	private FilterConfig filterConfig = null;
	/***/
	private static final ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<HttpServletRequest>();
	/***/
	private static final ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<HttpServletResponse>();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
	{
		try
		{
			if (log.isDebugEnabled())
			{
				log.debug("enter RequestFilter doFilter");
			}
			requestLocal.set((HttpServletRequest) request);
			responseLocal.set((HttpServletResponse) response);
			filterChain.doFilter(request, response);
		}
		catch (ServletException sx)
		{
			filterConfig.getServletContext().log(sx.getMessage());
		}
		catch (IOException iox)
		{
			filterConfig.getServletContext().log(iox.getMessage());
		}
		finally
		{
			requestLocal.set(null);
			responseLocal.set(null);
		}
	}

	@Override
	public void destroy()
	{
		requestLocal.set(null);
		responseLocal.set(null);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		this.filterConfig = filterConfig;
	}

	/**
	 * 获取当前线程的request
	 * 
	 * @return the requestLocal
	 */
	public static HttpServletRequest getRequest()
	{
		HttpServletRequest request = requestLocal.get();
		if (request == null)
		{
			log.warn("request在线程内没有找到,可能原因：1、当前调用没有处在web容器内；2、web.xml没有配置RequestFilter过滤器；");
		}
		return request;
	}

	/**
	 * 获取当前线程的reponse
	 * 
	 * @return the responseLocal
	 */
	public static HttpServletResponse getResponse()
	{
		HttpServletResponse response = responseLocal.get();
		if (response == null)
		{
			log.warn("response在线程内没有找到,可能原因：1、当前调用没有处在web容器内；2、web.xml没有配置RequestFilter过滤器；");
		}
		return response;
	}

	/**
	 * 获取当前线程的session
	 *
	 * @return the requestLocal.session
	 */
	public static HttpSession getSession(){
		HttpServletRequest request = requestLocal.get();
		if (request == null)
		{
			log.warn("session在线程内没有找到,可能原因：1、当前调用没有处在web容器内；2、web.xml没有配置RequestFilter过滤器；");
		}
		return request.getSession();
	}

	/**
	 * 打印当前的请求数据
	 *
	 * @return
	 */
	public static void printReq() {
		// request url
		HttpServletRequest request = requestLocal.get();
		StringBuffer buf = new StringBuffer();
		buf.append("------------- print request parameters ----------------\n");
		buf.append(request.getRequestURL());
		String queryStr = request.getQueryString();
		if (queryStr != null) {
			buf.append("?").append(queryStr);
		}

		buf.append("\n");

		// request parameters
		Map map = request.getParameterMap();
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Entry entry = (Entry)iter.next();
			String key = (String)entry.getKey();
			String[] values = (String[])entry.getValue();
			buf.append(key);
			buf.append(" = ");
			buf.append(Arrays.asList(values));
			buf.append("\n");
		}

		log.debug("\n" + buf);
	}

	/**
	 * 打印当前的请求url
	 * @return
	 */
	public static void printUrl() {
		HttpServletRequest request = requestLocal.get();
		StringBuffer reqUrl = request.getRequestURL();
		String queryStr = request.getQueryString();
		if (queryStr != null) {
			reqUrl.append("?").append(queryStr);
		}
		log.debug("\n" + reqUrl);
	}

	/**
	 * 打印当前的请求参数
	 * @return
	 */
	public static void printParams() {
		HttpServletRequest request = requestLocal.get();
		Map map = request.getParameterMap();
		StringBuffer paramInfo = new StringBuffer(256);
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Entry entry = (Entry)iter.next();
			String key = (String)entry.getKey();
			String[] values = (String[])entry.getValue();
			paramInfo.append(key);
			paramInfo.append(" = ");
			paramInfo.append(Arrays.asList(values));
			paramInfo.append("\n");
		}
		log.debug("\n" + paramInfo);
	}

	/**
	 * 获取当前的请求数据
	 * @return Map
	 */
	public static Map<String,Object> getRequestParam(){
		HttpServletRequest request = requestLocal.get();
		Enumeration<String> params= request.getParameterNames();
		Map<String,Object> result=new HashMap<String, Object>();
		while(params.hasMoreElements()){
			String key=params.nextElement();
			result.put(key, request.getParameter(key));
		}
		return result;
	}

	/**
	 * 获取当前的请求参数
	 * @return String
	 */
	public static String getRequestParams(){
		HttpServletRequest request = requestLocal.get();
		String result = "";
		Enumeration<String> params= request.getParameterNames();
		while(params.hasMoreElements()){
			String key=params.nextElement();
			result += key + "=" + request.getParameter(key) + "&";
		}
		if(result.length() > 0) {
			result = result.substring(0, result.length()-1);
		}
		return result;
	}

	/**
	 * 获取远程访问IP
	 * @return String
	 */
	public static String getRemoteIp(){
		HttpServletRequest request = requestLocal.get();
		String remoteIp = request.getHeader("x-forwarded-for");
		if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
			remoteIp= request.getHeader("X-Real-IP");
		}
		if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
			remoteIp= request.getHeader("Proxy-Client-IP");
		}
		if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
			remoteIp= request.getHeader("WL-Proxy-Client-IP");
		}
		if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
			remoteIp= request.getHeader("HTTP_CLIENT_IP");
		}
		if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
			remoteIp= request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
			remoteIp= request.getRemoteAddr();
		}
		if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
			remoteIp= request.getRemoteHost();
		}
		return remoteIp;
	}

}
