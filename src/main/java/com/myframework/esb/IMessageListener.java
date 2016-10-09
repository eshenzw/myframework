package com.myframework.esb;

public interface IMessageListener
{
	// 读取消息
	public boolean fetch(String key, String value);
}
