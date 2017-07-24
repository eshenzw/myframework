package com.myframework.core.alarm;

import org.springframework.beans.factory.InitializingBean;

/**
 * 事件上报的开关
 * 
 * created by zw
 *
 */
public class EventDeterminater implements InitializingBean {

	private static EventDeterminater instance;

	private boolean serviceAcessEventOpen;

	private boolean exceptionEventOpen;

	private boolean sessionExceptionEventOpen;
	
	private boolean agentServiceAccessOpen;
	
	private boolean rabbitMqSendEventOpen;

	

	public boolean isRabbitMqSendEventOpen() {
		return rabbitMqSendEventOpen;
	}

	public void setRabbitMqSendEventOpen(boolean rabbitMqSendEventOpen) {
		this.rabbitMqSendEventOpen = rabbitMqSendEventOpen;
	}

	public boolean isAgentServiceAccessOpen() {
		return agentServiceAccessOpen;
	}

	public void setAgentServiceAccessOpen(boolean agentServiceAccessOpen) {
		this.agentServiceAccessOpen = agentServiceAccessOpen;
	}

	public boolean isServiceAcessEventOpen() {
		return serviceAcessEventOpen;
	}

	public void setServiceAcessEventOpen(boolean serviceAcessEventOpen) {
		this.serviceAcessEventOpen = serviceAcessEventOpen;
	}

	public boolean isExceptionEventOpen() {
		return exceptionEventOpen;
	}

	public void setExceptionEventOpen(boolean exceptionEventOpen) {
		this.exceptionEventOpen = exceptionEventOpen;
	}

	public boolean isSessionExceptionEventOpen() {
		return sessionExceptionEventOpen;
	}

	public void setSessionExceptionEventOpen(boolean sessionExceptionEventOpen) {
		this.sessionExceptionEventOpen = sessionExceptionEventOpen;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		instance = new EventDeterminater();

		instance.serviceAcessEventOpen = this.serviceAcessEventOpen;

		instance.exceptionEventOpen = this.exceptionEventOpen;

		instance.sessionExceptionEventOpen = this.sessionExceptionEventOpen;
		
		instance.agentServiceAccessOpen = this.agentServiceAccessOpen;
		
		instance.rabbitMqSendEventOpen = this.rabbitMqSendEventOpen;
	}

	/**
	 * 判断此类事件是否需要上报
	 * 
	 * @param type
	 * @return
	 */
	public static boolean isEventPublishOpen(EventTypeEnum type) {

		//有可能未初始化
		if(instance == null) {
			return false;
		}
		
		switch (type) {

		case EVENT_TYPE_SESSION_EX:
			return instance.sessionExceptionEventOpen;

		case EVENT_TYPE_SERVICE_ACCESS:
			return instance.serviceAcessEventOpen;

		case EVENT_TYPE_SERVER_EX:
			return instance.exceptionEventOpen;
			
		case EVENT_TYPE_AGENT_SERVICE_ACCESS:
			return instance.agentServiceAccessOpen;
			
		case EVENT_TYPE_RABBIT_SEND:
			return instance.rabbitMqSendEventOpen;

		default:
			return true;

		}
	}

}