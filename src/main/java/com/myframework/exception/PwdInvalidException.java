package com.myframework.exception;

/*******************************************************************************
 * 密码失效
 * 
 * @author yinyj
 */
@SuppressWarnings("serial")
public class PwdInvalidException extends AuthFailureException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2535863235914354458L;

	/***************************************************************************
	 * @param message
	 *            异常描述信息
	 */
	public PwdInvalidException(String message)
	{
		super(message);
	}
}
