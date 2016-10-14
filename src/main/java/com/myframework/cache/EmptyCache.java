package com.myframework.cache;

import org.springframework.cache.Cache;

/**
 * Created by zw on 2015/10/14.
 */
public class EmptyCache implements Cache
{
	@Override public String getName()
	{
		return null;
	}

	@Override public Object getNativeCache()
	{
		return null;
	}

	@Override public ValueWrapper get(Object o)
	{
		return null;
	}

	@Override public <T> T get(Object o, Class<T> aClass)
	{
		return null;
	}

	@Override public void put(Object o, Object o1)
	{

	}

	@Override public void evict(Object o)
	{

	}

	@Override public void clear()
	{

	}
}
