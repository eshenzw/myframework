package com.myframework.core.alarm.monitor.interceptor;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.myframework.core.alarm.event.ServerExceptionEvent;

/**
 * 
 * created by zw
 *
 */
public abstract class AbstractGlobalExceptionHandler implements  ApplicationEventPublisherAware { 
	
    private final Logger logger = LoggerFactory.getLogger(getClass());

	
    private   ApplicationEventPublisher publisher;

    
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
           this.publisher = applicationEventPublisher;
    }
	
	/**
	 * 上报异常到 influxdb
	 * @param request
	 * @param exception
	 */
	protected void reportException(HttpServletRequest request,Exception exception) {
		
//        logger.error("*****************************unknown system exception happened at:{}", request.getRequestURI());
		
        publisher.publishEvent(new ServerExceptionEvent(request.getRequestURI(), exception));

	}
	
	

	
	

}
