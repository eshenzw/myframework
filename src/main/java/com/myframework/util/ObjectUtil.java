package com.myframework.util;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectUtil
{
	// 日志
	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectUtil.class);

	/**
	 * 检查参数（value）是否为空
	 * 
	 * @param value
	 *            检查值
	 * @return true:是，false:否
	 */
	public static boolean isEmpty(Object value)
	{
		return value == null ? true : value.toString().isEmpty() ? true : false;
	}

	public static boolean isNotEmpty(Object value)
	{
		return !isEmpty(value);
	}

	/**
	 * 将Object转换成字符串表示形式.
	 * 
	 * @param value
	 *            要转换的值
	 * 
	 * @return 返回Object字符串表示形式
	 */
	public static String toString(Object value)
	{
		if (value == null)
		{
			return null;
		}
		else
		{
			// 判断是否是BigDecimal
			if (value instanceof BigDecimal)
			{
				// 当BigDecimal位数过长时会用科学记数法记数，当然这种记数方法不是系统相要的，所以用toPlainString方法返回不带指数的字符串表示形式
				return ((BigDecimal) value).toPlainString();
			}
			else
			{

				return value.toString();
			}
		}
	}

	/**
	 * 如要转换的Object为null可用defaultValue作为Object字符串表示形式，反之则调用toString(Object
	 * value)进行转换.
	 * 
	 * @param value
	 *            要转换的值
	 * @param defaultValue
	 *            value 为空时用来表示Object字符串表示形式的值
	 * 
	 * @return 返回Object字符串表示形式
	 */
	public static String toString(Object value, String defaultValue)
	{
		if (value != null)
		{
			return value.toString();
		}
		else
		{
			return defaultValue;
		}
	}

	public static Long toLong(Object value)
	{
		try
		{
			return StringUtil.isEmpty(value) ? null : Long.valueOf(value.toString());
		}
		catch (NumberFormatException e)
		{
			LOGGER.error(String.format("%s不能转换成Long", value), e);
			return null;
		}
	}
}
