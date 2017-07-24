package com.myframework.core.alarm.event.handler;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.myframework.core.alarm.event.AgentServiceEvent;

/**
 * 前置机 服务事件处理
 * 
 * created by zw
 *
 */
public class AgentServiceEventHandler extends AbstractEventHandler<AgentServiceEvent> {

	/**
	 * 设置参数
	 */
	@Override
	protected void addArgs(AgentServiceEvent event, Map<String, Object> args) {

		args.put("result", event.getResult());

		args.put("stack", event.getStack());

		if (StringUtils.isNotEmpty(event.getExtra())) {
			args.put("extra", event.getExtra());
		}

	}

	/**
	 * 设置tags
	 */
	@Override
	protected void addTags(AgentServiceEvent event, Map<String, String> tags) {
		tags.put("tenant_id", String.valueOf(event.getTenantId()));
	}

	/**
	 * 仅仅记录非{@link AgentServiceEvent}的异常
	 * 
	 * @param event
	 *            事件
	 * @return 是否忽略这个事件
	 */
	protected boolean isIgnore(final AgentServiceEvent event) {

		return false;

	}

	@Override
	String getCatalog(AgentServiceEvent event) {
		return "agent_service_access";

	}
}
