package com.myframework.notify.sms;

import java.util.Map;

/**
 * 短信发送器接口
 * 
 * @author zw
 * 
 */
public interface ISmsSender
{
	/**
	 * 发送短信实现
	 * 
	 * @param paramSmsBean
	 *            短信发送Bean
	 * @return Map 返回信息
	 */
	Map sendSMS(SmsBean paramSmsBean);
}
