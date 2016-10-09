package com.myframework.exception;

/*******************************************************************************
 * 用户session异常
 * 
 * @author jinhao
 */
@SuppressWarnings("serial")
public class UserSessionNotFoundException extends RuntimeException
{
	/***************************************************************************
	 * 
	 */
	public UserSessionNotFoundException()
	{
		super();
	}

	/***************************************************************************
	 * @param message
	 *            异常描述信息、
	 * @param cause
	 *            导致此异常的异常
	 */
	public UserSessionNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/***************************************************************************
	 * @param message
	 *            异常描述信息、
	 */
	public UserSessionNotFoundException(String message)
	{
		super(message);
	}

	/***************************************************************************
	 * @param cause
	 *            导致此异常的异常
	 */
	public UserSessionNotFoundException(Throwable cause)
	{
		super(cause);
	}
}
