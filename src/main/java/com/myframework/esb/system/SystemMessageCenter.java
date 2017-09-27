package com.myframework.esb.system;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myframework.esb.IMessageCenter;

public class SystemMessageCenter implements IMessageCenter
{
	private static Logger logger = LoggerFactory.getLogger(SystemMessageCenter.class);

	@Override
	public void send(String topic, String key, String message)
	{
		logger.warn("!!!!系统消息中间件出现异常!!!!");
	}

	@Override
	public void sendMulti(String topic, String key, List<String> messages)
	{
		logger.warn("!!!!系统消息中间件出现异常!!!!");
	}

	@Override
	public void initialize() {

	}

	@Override
	public void destroy() {

	}
}
