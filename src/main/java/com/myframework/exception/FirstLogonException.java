package com.myframework.exception;

/*******************************************************************************
 * 第一次登录异常
 * 
 * @author yinyj
 */
@SuppressWarnings("serial")
public class FirstLogonException extends AuthFailureException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2535863235914354458L;

	/***************************************************************************
	 * @param message
	 *            异常描述信息
	 */
	public FirstLogonException(String message)
	{
		super(message);
	}
}
