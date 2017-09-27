package com.myframework.esb;

import java.util.List;

public interface IMessageCenter
{
	public void initialize();
	public void destroy();
	/**
	 * 发送单条消息
	 * 
	 * @param topic
	 *            发送主题
	 * @param key
	 *            发送键值
	 * @param message
	 *            消息内容
	 * @throws MessageCenterException
	 */
	public void send(String topic, String key, String message) throws MessageCenterException;

	/**
	 * 发送多条消息
	 * 
	 * @param topic
	 *            发送主题
	 * @param key
	 *            发送键值
	 * @param messages
	 *            消息内容
	 * @throws MessageCenterException
	 */
	public void sendMulti(String topic, String key, List<String> messages) throws MessageCenterException;

}
