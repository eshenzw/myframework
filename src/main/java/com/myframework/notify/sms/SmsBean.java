package com.myframework.notify.sms;

import java.util.ArrayList;
import java.util.List;

/**
 * 短信发送Bean
 * 
 * @author zw
 * @version 1.0
 * 
 */
public class SmsBean
{
	/**
	 * 发送号码
	 */
	private List<String> destinationAddresses;
	/**
	 * 扩展码
	 */
	private String extendCode;
	/**
	 * 消息
	 */
	private String message;
	/**
	 * 消息格式化 ASCII,UCS2,GB18030,GB2312,Binary
	 */
	private String messageFormat;

	/**
	 * 发送方法 Normal,Instant,Long,Structured
	 */
	private String sendMethod;
	/**
	 * 是否要状态报告
	 */
	private Boolean deliveryResultRequest;

	/**
	 * 短信的宿主功能
	 */
	private String operateType;

	public String getOperateType()
	{
		return operateType;
	}

	public void setOperateType(String operateType)
	{
		this.operateType = operateType;
	}

	public String getExtendCode()
	{
		return extendCode;
	}

	public void setExtendCode(String extendCode)
	{
		this.extendCode = extendCode;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * @return the deliveryResultRequest
	 */
	public Boolean isDeliveryResultRequest()
	{
		return deliveryResultRequest;
	}

	/**
	 * @param deliveryResultRequest
	 *            the deliveryResultRequest to set
	 */
	public void setDeliveryResultRequest(Boolean deliveryResultRequest)
	{
		this.deliveryResultRequest = deliveryResultRequest;
	}

	/**
	 * @return the messageFormat
	 */
	public String getMessageFormat()
	{
		return messageFormat;
	}

	/**
	 * @param messageFormat
	 *            the messageFormat to set
	 */
	public void setMessageFormat(String messageFormat)
	{
		this.messageFormat = messageFormat;
	}

	/**
	 * @return the sendMethod
	 */
	public String getSendMethod()
	{
		return sendMethod;
	}

	/**
	 * @param sendMethod
	 *            the sendMethod to set
	 */
	public void setSendMethod(String sendMethod)
	{
		this.sendMethod = sendMethod;
	}

	/**
	 * 获取地址列表，如果不存在则新建
	 * 
	 * @return 地址列表集合
	 */
	public List<String> getDestinationAddresses()
	{
		if (destinationAddresses == null)
		{
			destinationAddresses = new ArrayList<String>();
		}
		return this.destinationAddresses;
	}

}
