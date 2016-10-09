package com.myframework.util;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数值操作工具类
 * 
 * @author fengjun
 * @version 1.0
 * @Date:2011-8-23下午05:02:57
 */
public final class BigDecimalUtils
{
	/** 日志. */
	private static final Logger LOGGER = LoggerFactory.getLogger(BigDecimalUtils.class);

	/**
	 * 数值格式化
	 * 
	 * @param source
	 *            需格式化的字符串
	 * @param decimals
	 *            精度
	 * @return
	 */
	public static String numberFormat(String source, int decimals)
	{
		if (StringUtil.isNotEmpty(source) && StringUtil.isNotEmpty(decimals))
		{
			try
			{
				BigDecimal bd = new BigDecimal(source);
				bd = bd.setScale(decimals, BigDecimal.ROUND_DOWN);
				if (bd != null)
				{
					return bd.toString();
				}
			}
			catch (RuntimeException e)
			{
				if (LOGGER.isErrorEnabled())
				{
					LOGGER.error("将{}格式化成精度为{}的数值时出现错误！", source, decimals);
				}
			}
		}
		return null;
	}

	/**
	 * 把给定的值转换成BigDecimal类型.
	 * 
	 * @param value
	 *            需转换的值
	 * @return the 返回BigDecimal类型的值
	 */
	public static BigDecimal toBigDecimal(Object value)
	{
		// 如果为空默认为0.
		if (StringUtil.isEmpty(value))
		{
			return new BigDecimal(0);
		}
		// 如果需转换的值实际类型是Bigdecimal，则做强转就可以了。
		if (value instanceof BigDecimal)
		{
			return (BigDecimal) value;
		}
		try
		{
			return new BigDecimal(value.toString());
		}
		catch (RuntimeException e)
		{
			LOGGER.error(String.format("无法把%s转换成BigDecimal", value), e);
			throw new RuntimeException("数据转换异常，请与管理员联系！");
		}
	}
}
