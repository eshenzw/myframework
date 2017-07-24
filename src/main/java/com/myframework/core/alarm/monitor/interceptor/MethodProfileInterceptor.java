package com.myframework.core.alarm.monitor.interceptor;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.myframework.core.alarm.EventDeterminater;
import com.myframework.core.alarm.EventTypeEnum;
import com.myframework.core.alarm.monitor.ThreadProfile;

/**
 * 对方法进行性能统计的拦截器，spring和struts2 通用
 * 
 * created by zw
 *
 */
public class MethodProfileInterceptor implements MethodInterceptor, InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		/**
		 * 被代理的方法的，类名、方法名、参数
		 */
		String className = invocation.getMethod().getDeclaringClass().getName();
		String method = invocation.getMethod().getName();

		boolean isInExclude = AbstractThreadProfileInterceptor.isInServiceAccessExclude();

		boolean isServiceAccessOpen = EventDeterminater.isEventPublishOpen(EventTypeEnum.EVENT_TYPE_SERVICE_ACCESS);
		
		//某些请求堆栈太长，不打service层的堆栈
		if (!isInExclude&&isServiceAccessOpen) {
			/**
			 * 全局性能统计，并记录堆栈，函数调用开始
			 */
			ThreadProfile.enter(className, method);
		}

		/**
		 * 返回结果
		 */
		Object result = null;
		try {

			/**
			 * 执行被代理（拦截）的调用
			 */
			result = invocation.proceed();

		} catch (Exception e) {
			throw e;
		} finally {

			//不打service层的堆栈
			if (!isInExclude&&isServiceAccessOpen) {
				/**
				 * 全局性能统计，并记录堆栈，函数调用结束
				 */
				ThreadProfile.exit();
			}

		}

		return result;
	}

}
