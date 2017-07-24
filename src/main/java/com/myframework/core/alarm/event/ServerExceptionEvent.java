package com.myframework.core.alarm.event;

import com.myframework.core.alarm.EventTypeEnum;

/**
 * 服务模块，对外提供http服务，出现异常的事件
 * <p/>
 * Created by zhangjun
 */
public class ServerExceptionEvent extends CommonEvent {

    /**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = -6906935866415083159L;
	/**
     * RequestURI
     */
    private final String serviceName;
    
    private final Throwable exception;

    /**
     * Create a new ApplicationEvent.
     *
     * @param name : requestURI
     * @param  exception: 异常
     */
    public ServerExceptionEvent(String name, Throwable exception) {
        super(name);
        this.serviceName = name;
        this.exception = exception;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Throwable getException() {
        return exception;
    }
    
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.EVENT_TYPE_SERVER_EX;
	}

}
