package com.myframework.notify.sms;

import com.myframework.notify.ISendMessageThread;

/**
 * 短信发送线程对象
 * 
 * @author zw
 * @version 1.0
 * 
 */
public class SmsSendThread implements ISendMessageThread
{
	/**
	 * 短信发送对象
	 */
	private ISmsSender sender;

	/**
	 * 参数Bean
	 */
	private SmsBean smsBean;

	/**
	 * @return the sender
	 */
	public ISmsSender getSender()
	{
		return sender;
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(ISmsSender sender)
	{
		this.sender = sender;
	}

	/**
	 * @return the smsBean
	 */
	public SmsBean getSmsBean()
	{
		return smsBean;
	}

	/**
	 * @param smsBean
	 *            the smsBean to set
	 */
	public void setSmsBean(SmsBean smsBean)
	{
		this.smsBean = smsBean;
	}

	@Override
	public void run()
	{
		sender.sendSMS(this.smsBean);
	}

}
