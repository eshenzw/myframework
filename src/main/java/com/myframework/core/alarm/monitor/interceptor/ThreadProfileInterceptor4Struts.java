/*
package com.myframework.core.alarm.monitor.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;


*/
/**
 * strtus框架使用此 interceptor
 * 
 * 服务的interceptor，用来做慢的服务调用统计
 * created by zw
 *
 *//*

public class ThreadProfileInterceptor4Struts extends AbstractThreadProfileInterceptor implements Interceptor{

	*/
/**
	 * 
	 *//*

	private static final long serialVersionUID = 3002142764420174465L;

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
		
		//step1
		this.doPreHandle(request);
		
		try {
		    String invokeStr = invocation.invoke();
		    
		    return invokeStr;
	
		}finally {
			doAfterCompletion(request, response);
		}
	}

	
}*/
