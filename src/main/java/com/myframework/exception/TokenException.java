package com.myframework.exception;

/**
 * Created by zw on 2017/7/22.
 */
public class TokenException extends Exception
{
	public TokenException()
	{
		super();
	}

	public TokenException(String message)
	{
		super(message);
	}

	public TokenException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public TokenException(Throwable cause)
	{
		super(cause);
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
