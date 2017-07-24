package com.myframework.core.alarm.event;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.context.ApplicationEvent;

import com.myframework.core.alarm.EventTypeEnum;

/**
 *
 * 通用的事件
 *
 * Created by zw.
 */
public abstract class CommonEvent extends ApplicationEvent {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 2840308379764797160L;
	
	// 什么事件
	protected final String name;

	/**
	 * Create a new ApplicationEvent.
	 *
	 * @param name
	 *            : 事件的名称
	 */
	public CommonEvent(String name) {
		super(name);

		this.name = name;

	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getName() {
		return name;
	}
	
	
	public abstract EventTypeEnum getEventType() ;
	
}
