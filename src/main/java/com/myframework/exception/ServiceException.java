package com.myframework.exception;

/**
 * Created by zw on 2015/9/22.
 */
public class ServiceException extends Exception
{
	/**
	 *
	 */
	private static final long serialVersionUID = -206524996050590111L;

	/**
	 * Constructor for ServiceException.
	 *
	 * @param msg
	 *            the detail message
	 */
	public ServiceException(String msg)
	{
		super(msg);
	}

	/**
	 * Constructor for ServiceException.
	 *
	 * @param cause
	 *            异常对象
	 */
	public ServiceException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * Constructor for ServiceException.
	 *
	 * @param msg
	 *            the detail message
	 * @param cause
	 *            异常对象
	 */
	public ServiceException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
