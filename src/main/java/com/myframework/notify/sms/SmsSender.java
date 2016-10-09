package com.myframework.notify.sms;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 短信发送器
 * 
 * @author zw
 * 
 */
public class SmsSender implements ISmsSender
{
	/**
	 * 日志信息
	 */
	private static final Logger LOGGER = Logger.getLogger(SmsSender.class);

	/**
	 * 是否要状态报告
	 */
	private Boolean deliveryResultRequest;

	/**
	 * 扩展码
	 */
	private String extendCode;

	/**
	 * @param messageFormat
	 *            the messageFormat to set
	 */
	public void setMessageFormat(String messageFormat)
	{

	}

	/**
	 * @param sendMethod
	 *            the sendMethod to set
	 */
	public void setSendMethod(String sendMethod)
	{

	}

	/**
	 * @param deliveryResultRequest
	 *            the deliveryResultRequest to set
	 */
	public void setDeliveryResultRequest(Boolean deliveryResultRequest)
	{
		this.deliveryResultRequest = deliveryResultRequest;
	}

	public void init()
	{

	}

	@Override
	public Map sendSMS(SmsBean paramSmsBean)
	{
		return null;
	}

}
