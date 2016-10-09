package com.myframework.util;

import java.util.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;

/**
 * 
 * @描述：集合工具类
 */
public final class CollectionUtil
{

	/**
	 * 私有构造函数
	 */
	private CollectionUtil()
	{

	}

	/**
	 * Checks if is empty.
	 * 
	 * @param collection
	 *            the collection
	 * 
	 * @return true, if is empty
	 */
	public static boolean isEmpty(Collection<?> collection)
	{
		return collection == null || collection.isEmpty();
	}

	/**
	 * 判断是否非空
	 * 
	 * @param collection
	 *            集合
	 * @return isNotEmpty
	 */
	/**
	 * Checks if is not empty.
	 * 
	 * @param collection
	 *            the collection
	 * 
	 * @return true, if is not empty
	 */
	public static boolean isNotEmpty(Collection<?> collection)
	{
		return !isEmpty(collection);
	}

	/**
	 * Checks if is empty.
	 * 
	 * @param map
	 *            the map
	 * 
	 * @return true, if is empty
	 */
	public static boolean isEmpty(Map<?, ?> map)
	{
		return map == null || map.isEmpty();
	}

	/**
	 * Contains.
	 * 
	 * @param coll
	 *            the coll
	 * @param propertyName
	 *            the property name
	 * @param o
	 *            the o
	 * 
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public static <T> T contains(Collection<T> coll, String propertyName, Object o)
	{
		Iterator<?> it = coll.iterator();
		while (it.hasNext())
		{
			try
			{
				T element = (T) it.next();
				String value = BeanUtils.getProperty(element, propertyName);
				if (o == null ? value == null : o.equals(value))
				{
					return element;
				}
			}
			catch (Exception e)
			{
				continue;
			}
		}
		return null;
	}

	/**
	 * 并集
	 * 
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Object> Collection<T> unionAll(final Collection<T> a, final Collection<T> b)
	{
		ArrayList<T> list = new ArrayList<T>(a);
		list.addAll(b);
		return list;
	}

	public static <T> List<T> typedList(Collection<? extends Object> collection, Class<T> type)
	{
		return new ArrayList<T>(CollectionUtils.typedCollection(collection, type));
	}

	public static <T> List<T> distinct(Collection<T> collection)
	{
		return new ArrayList<T>(new HashSet<T>(collection));
	}

}
