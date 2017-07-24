package com.myframework.core.alarm.event;

import com.myframework.core.alarm.EventTypeEnum;

/**
 * 前置机服务访问事件
 * 
 * created by zw
 *
 */
public class AgentServiceEvent extends CommonEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3090662657691915559L;

	/**
	 * 执行结果
	 */
	private String result;

	/**
	 * 堆栈信息
	 */
	private String stack;

	/**
	 * 企业id
	 */
	private long tenantId;
	
	/**
	 * 补充信息
	 */
	private String extra;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getStack() {
		return stack;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}

	public long getTenantId() {
		return tenantId;
	}

	public void setTenantId(long tenantId) {
		this.tenantId = tenantId;
	}

	
	
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	
	/**
	 * 前置机service事件
	 * 
	 * @param interfaceName
	 *            接口名
	 * @param result
	 *            请求结果
	 * @param stack
	 *            堆栈
	 * @param tenantId
	 *            企业id
	 */
	public AgentServiceEvent(String interfaceName, String result, String stack, long tenantId) {
		super(interfaceName);
		this.result = result;
		this.stack = stack;
		this.tenantId = tenantId;
		
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.EVENT_TYPE_AGENT_SERVICE_ACCESS;
	}

}