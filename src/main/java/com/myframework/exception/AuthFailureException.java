package com.myframework.exception;

/*******************************************************************************
 * 认证异常
 * 
 * @author yinyj
 */
@SuppressWarnings("serial")
public class AuthFailureException extends ChainFailureException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2535863235914354458L;

	/***************************************************************************
	 * @param message
	 *            异常描述信息
	 */
	public AuthFailureException(String message)
	{
		super("登录失败，失败原因为：" + message);
	}
}
