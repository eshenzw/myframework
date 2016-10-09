package com.myframework.extend;

import java.util.HashMap;

/**
 * 从写HashMap在put和get时统一了他们都小写， 这样保存和取出的时候就不分大小写了。
 * 
 * @DateTime: 2011-3-4
 * @author author
 * @version 1.0
 * @param <K>
 * @param <V>
 */
public class CaseInsensitiveHashMap<K, V> extends HashMap
{
	@Override
	public Object put(Object key, Object value)
	{
		return super.put(key.toString().toLowerCase(), value);
	}

	@Override
	public Object get(Object key)
	{
		return super.get(key.toString().toLowerCase());
	}
}
