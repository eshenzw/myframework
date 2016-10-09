package com.myframework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 替换所需分页对象
 * 
 * @author zhaomin
 * @version 1.0
 */
public final class ReflectUtil
{
	/**
	 * 私有构造函数
	 */
	private ReflectUtil()
	{

	}

	/**
	 * 日志信息
	 */
	private static final Log LOGGER = LogFactory.getLog(ReflectUtil.class);

	/**
	 * 设置参数
	 * 
	 * @param target
	 *            目标
	 * @param fname
	 *            名字
	 * @param ftype
	 *            类型
	 * @param fvalue
	 *            值
	 */
	public static void setFieldValue(Object target, String fname, Class ftype, Object fvalue)
	{
		if (target == null || fname == null || "".equals(fname)
				|| (fvalue != null && !ftype.isAssignableFrom(fvalue.getClass())))
		{
			return;
		}
		Class clazz = target.getClass();
		try
		{
			Field field = clazz.getDeclaredField(fname);
			if (!Modifier.isPublic(field.getModifiers()))
			{
				field.setAccessible(true);
			}
			field.set(target, fvalue);

		}
		catch (Exception e)
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(e);
			}
		}
	}
}
