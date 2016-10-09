package com.myframework.exception;

/**
 * 没有找到当前的K
 * 
 * @author 顾林
 */
public class KeyNotFoundException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6742797019090530479L;

	/**
	 * 主键未找到异常
	 * 
	 * @param message
	 *            异常消息
	 */
	public KeyNotFoundException(String message)
	{
		super("参数没有找到KEY： " + message);
	}
}
