package com.myframework.exception;

/*******************************************************************************
 * 认证链异常
 * 
 * @author yinyj
 */
@SuppressWarnings("serial")
public class ChainFailureException extends Exception
{
	/***************************************************************************
	 * @param message
	 *            异常描述信息
	 */
	public ChainFailureException(String message)
	{
		super(message);
	}

	/***************************************************************************
	 * @param message
	 *            异常描述信息、
	 * @param ex
	 *            导致此异常的异常
	 */
	public ChainFailureException(String message, Exception ex)
	{
		super(message, ex);
	}

	/**
	 * 获取原始的消息 避免验证链加上“剩余**次”的信息
	 * 
	 * @return
	 */
	public String getOriginalMessage()
	{
		return super.getMessage();
	}
}
