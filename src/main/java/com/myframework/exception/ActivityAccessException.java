package com.myframework.exception;

/*******************************************************************************
 * 访问控制异常
 * 
 * @author jinhao
 */
@SuppressWarnings("serial")
public class ActivityAccessException extends Exception
{
	/**
	 * 
	 */
	public ActivityAccessException()
	{
		super();
	}

	/***************************************************************************
	 * @param message
	 *            异常描述信息
	 */
	public ActivityAccessException(String message)
	{
		super(message);
	}
}
