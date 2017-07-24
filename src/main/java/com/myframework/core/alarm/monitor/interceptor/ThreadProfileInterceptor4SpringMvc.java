package com.myframework.core.alarm.monitor.interceptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *  springmvc框架使用此 interceptor
 *
 *  服务的interceptor，用来做慢的服务调用统计
 *
 * 
 */
public class ThreadProfileInterceptor4SpringMvc extends AbstractThreadProfileInterceptor implements HandlerInterceptor {


    protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		this.doPreHandle(request);		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {


	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		this.doAfterCompletion(request, response);
	}

}
