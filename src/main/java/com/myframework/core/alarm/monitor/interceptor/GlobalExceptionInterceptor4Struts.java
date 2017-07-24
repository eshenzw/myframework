package com.myframework.core.alarm.monitor.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 全局的异常处理抽象类，给struts2 使用
 * 
 * created by zw
 *
 */
public  class GlobalExceptionInterceptor4Struts extends AbstractGlobalExceptionHandler implements Interceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4666966617402976323L;
	

	@Override
	public void destroy() {
		
	}

	@Override
	public void init() {
		
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		ActionContext actionContext = invocation.getInvocationContext();
		
	    HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);  
		HttpServletResponse response = (HttpServletResponse) actionContext.get(ServletActionContext.HTTP_RESPONSE);  


		try {
		    String invokeStr = invocation.invoke();
		    
		    return invokeStr;
	
		} catch(Exception e) {
			return resolveException(request,response,e);
		}
		
	}
	
	
	/**
	 * 处理异常
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	private String resolveException(HttpServletRequest request,HttpServletResponse response, Exception e) throws Exception {

		boolean isNormalServiceException = isNormalServiceException(e);
		//业务异常不需要上报，只上报系统异常
		if(!isNormalServiceException) {
			reportException(request, e);
		}
		
		return doRealResolveException(request,response,e);
	}
	
	/**
	 * 实际处理异常，是直接抛出，还是做进一步的处理，业务模块可以重写此方法
	 * 
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 * @throws Exception
	 */
	public String doRealResolveException(HttpServletRequest request,HttpServletResponse response, Exception e) throws Exception {
		throw e;
	}
	
	
	/**
	 * 判断是否是业务异常，业务异常不需要上报，非业务异常需要上报到 influxdb.
	 * 
	 * 业务可以重写此方法。默认所有捕获到的异常都是非业务异常。
	 * 
	 * @param exception
	 * @return
	 */
	protected boolean isNormalServiceException(Exception exception) {
		
		return false;
	}
}
