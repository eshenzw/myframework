package com.myframework.core.alarm;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import com.myframework.core.alarm.event.CommonEvent;

/**
 * 
 * 上报事件
 * created by zw
 *
 */
public class EventPublisherUtils implements ApplicationEventPublisherAware {
	
    private static EventPublisherUtils instance;
	
	private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		
		instance = new EventPublisherUtils();

		this.applicationEventPublisher = applicationEventPublisher;
		
		instance.applicationEventPublisher = applicationEventPublisher;
		
	}
	
	
	
	/**
	 * 上报事件
	 * @param event
	 */
	public static void reportEvent(ApplicationEvent event) {
		
		if(isNeedPublishEvent(event))
		    instance.applicationEventPublisher.publishEvent(event);
		
	}
	
	/**
	 *  是否需要上报事件
	 * @param event
	 * @return
	 */
	private static boolean isNeedPublishEvent(ApplicationEvent event) {
		
		if(event instanceof CommonEvent) {
			
			return EventDeterminater.isEventPublishOpen(((CommonEvent) event).getEventType());
		}

		return false;

	}
	
}