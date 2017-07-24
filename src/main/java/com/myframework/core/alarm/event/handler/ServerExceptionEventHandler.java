package com.myframework.core.alarm.event.handler;

import com.myframework.core.alarm.event.ServerExceptionEvent;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Map;

/**
 * 从服务端统计： 服务端 spring mvc,struts2 出现异常的时候触发事件
 *
 * Created by zhangjun
 */
public class ServerExceptionEventHandler extends AbstractEventHandler<ServerExceptionEvent> {

	/**
	 * 设置参数
	 */
	@Override
	protected void addArgs(ServerExceptionEvent event, Map<String, Object> args) {
		args.put("stack", ExceptionUtils.getStackTrace(event.getException()));
		
	}

	/**
	 * 设置tags
	 */
	@Override
	protected void addTags(ServerExceptionEvent event, Map<String, String> tags) {
		tags.put("service_name", event.getServiceName());
	}

	@Override
	String getCatalog(ServerExceptionEvent event) {
		return "server_exception";
	}

	/**
	 * 仅仅记录非{@link ServiceException}的异常
	 * 
	 * @param event
	 *            事件
	 * @return 是否忽略这个事件
	 */
	protected boolean isIgnore(final ServerExceptionEvent event) {

		if (event.getException() == null) {
			return true;
		}
		return false;

	}

}
