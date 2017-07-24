package com.myframework.core.alarm.monitor.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;

/**
 * 统一的异常处理器抽象类 适配springmvc
 * created by zw
 *
 */
public class GlobalExceptionInterceptor4SpringMvc extends AbstractGlobalExceptionHandler
		implements HandlerExceptionResolver {
	

	@Override
	public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object handler, Exception exception) {
		
		//异常报警上报
		//判断如果不是正常的业务异常，那么需要上报告警信息
		
		boolean isNormalServiceException = isNormalServiceException(exception);
		if(!isNormalServiceException) {
		     reportException(httpServletRequest, exception);
		}
		
		//返回默认的model
        return getDefaultModelAndView4Exception(httpServletRequest, httpServletResponse, handler, exception);
	
	}
	
	
	
	/**
	 * 业务模块可以重写此方法，用来控制发生系统级异常时的返回
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param handler
	 * @param exception
	 * @return
	 */
	public ModelAndView getDefaultModelAndView4Exception(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object handler, Exception exception) {
		
        ModelAndView modelAndView = new ModelAndView();
        FastJsonJsonView jsonView = new FastJsonJsonView();
        Map<String, Object> errorInfoMap = new HashMap<>();

        jsonView.setAttributesMap(errorInfoMap);
        modelAndView.setView(jsonView);
        return modelAndView;
		
	}
	


	/**
	 * 判断是否是业务异常，业务异常不需要上报，非业务异常需要上报到 influxdb.
	 * 
	 * 业务可以重写此方法。默认所有捕获到的异常都是非业务异常。
	 * 
	 * 因为在springmvc框架中，此方法默认一定会触发，所以这里设置为  默认不上报事件
	 * 
	 * 
	 * @param exception
	 * @return
	 */
	protected boolean isNormalServiceException(Exception exception) {
		
		return true;
	}
}