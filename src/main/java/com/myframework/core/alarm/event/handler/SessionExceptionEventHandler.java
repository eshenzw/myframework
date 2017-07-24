package com.myframework.core.alarm.event.handler;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.myframework.core.alarm.event.SessionExceptionEvent;

/**
 * 
 * Session异常事件上报
 * 
 * created by zw
 *
 */
public class SessionExceptionEventHandler extends AbstractEventHandler<SessionExceptionEvent> {

	@Override
	String getCatalog(SessionExceptionEvent event) {

		return "session_exception";
	}

	/**
	 * 设置tags
	 */
	@Override
	protected void addTags(SessionExceptionEvent event, Map<String, String> tags) {
		// tags.put("type", String.valueOf(event.getType()));
		tags.put("error_code", String.valueOf(event.getErrorCode()));
	}

	/**
	 * 设置参数
	 */
	@Override
	protected void addArgs(SessionExceptionEvent event, Map<String, Object> args) {

		args.put("stack", ExceptionUtils.getStackTrace(event.getException()));
		args.put("src", event.getRequestUrl());
		args.put("method_info", event.getName());
		args.put("param", event.getParam());

	}

	/**
	 * 忽略某个事件
	 *
	 * @param event
	 *            事件
	 * @return 是否忽略这个事件
	 */
	protected boolean isIgnore(final SessionExceptionEvent event) {

		return false;
	}

}
