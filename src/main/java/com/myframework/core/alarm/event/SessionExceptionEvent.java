package com.myframework.core.alarm.event;

import com.myframework.core.alarm.EventTypeEnum;

/**
 * session 异常事件
 * 
 * created by zw
 *
 */
public class SessionExceptionEvent extends CommonEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2084377855324316087L;

	/**
	 * redis连接异常
	 */
	public static final int TYPE_REDIS_EX = 0;

	/**
	 * token校验失败
	 */
	public static final int TYPE_TOKEN_AUTH_FAILED = 1;
	
	
	/**
	 * refresh token校验失败
	 */
	public static final int TYPE_REFRESH_TOKEN_AUTH_FAILED = 2;

//	/**
//	 * 0-redis连接异常 1-token校验失败 2-refresh token校验失败
//	 */
//	private final int type;

	/**
	 * 请求url
	 */
	private final String requestUrl;

	/**
	 * 异常信息
	 */
	private final Throwable exception;
	
	/**
	 * 错误码
	 */
	private final int errorCode;

	/**
	 * 失败的请求的参数
	 * 
	 * @param name
	 */
	private final String param;
	
	/**
	 * 方法信息
	 */
	private final String methodInfo;


	public String getMethodInfo() {
		return methodInfo;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

//	public int getType() {
//		return type;
//	}

	public Throwable getException() {
		return exception;
	}

	public String getParam() {
		return param;
	}

	
	public int getErrorCode() {
		return errorCode;
	}


	
	
	
	/**
	 * 构造函数
	 * 
	 * @param name
	 * @param requestUrl
	 * @param param
	 * @param exception
	 */
	public SessionExceptionEvent(int type, String requestUrl, String methodInfo, String param, int errorcode,Throwable exception) {
		super(String.valueOf(type));
//		this.type = type;
		this.requestUrl = requestUrl;
		this.param = param;
		this.exception = exception;
		this.errorCode = errorcode;
		this.methodInfo = methodInfo;
	}
	
	
    
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.EVENT_TYPE_SESSION_EX;
	}

}