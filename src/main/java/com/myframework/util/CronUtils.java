package com.myframework.util;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronExpression;

/**
 * A utility class for Cron Expressions.
 * 
 * @author lixiaoyu
 */
public final class CronUtils
{
	/**
	 * 私有构造函数
	 */
	private CronUtils()
	{
	}

	/**
	 * Returns a boolean value representing the validity of a given Cron
	 * Expression
	 * 
	 * @param cronExpression
	 *            A Cron Expression
	 * @return boolean - Is given expression valid
	 */
	public static boolean isValid(String cronExpression)
	{
		return CronExpression.isValidExpression(cronExpression);
	}

	/**
	 * Returns the next execution time based on the given Cron Expression
	 * 
	 * @param cronExpression
	 *            A Cron Expression
	 * @return Date - The next time the given Cron Expression should fire
	 */
	public static Date getNextExecution(String cronExpression)
	{
		try
		{
			CronExpression cron = new CronExpression(cronExpression);
			return cron.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
		}
		catch (ParseException e)
		{
			throw new IllegalArgumentException(e.getMessage());
		}
	}
}
