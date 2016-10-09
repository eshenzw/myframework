package com.myframework.exception;

/*******************************************************************************
 * 运行时异常
 * 
 * @author jinhao
 */
@SuppressWarnings("serial")
public class RtException extends RuntimeException
{
	/***************************************************************************
	 * 
	 */
	public RtException()
	{
		super();
	}

	/***************************************************************************
	 * @param message
	 *            异常描述信息
	 */
	public RtException(String message)
	{
		super(message);
	}

	/***************************************************************************
	 * @param message
	 *            异常描述信息、
	 * @param cause
	 *            导致此异常的异常
	 */
	public RtException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * 获取异常描述信息 *
	 * 
	 * @return 描述信息
	 */
	@Override
	public String getMessage()
	{
		String msg = super.getMessage();
		if (getCause() != null)
		{
			msg = msg + ": " + getCause().getMessage();
		}
		return msg;
	}
}
