package com.myframework.event;

import java.io.Serializable;

import com.myframework.core.db.DynamicDataSource;
import com.myframework.core.filter.RequestFilter;

/***
 * 事件消息体
 * 
 * @author zw
 * 
 */
public class MyEvent implements Serializable
{
	private static final long serialVersionUID = 8256916968264450362L;
	protected String name;
	protected Long connId;
	protected Serializable data;

	public MyEvent(String name)
	{
		if (name == null)
			throw new IllegalArgumentException("事件名称不得为空");
		this.name = name;
		this.connId = (Long) RequestFilter.getSession().getAttribute(DynamicDataSource.SESSION_CONN_KEY);
	}

	public MyEvent(String name, Serializable data)
	{
		if (name == null)
			throw new IllegalArgumentException("事件名称不得为空");
		this.name = name;
		this.data = data;
		this.connId = (Long) RequestFilter.getSession().getAttribute(DynamicDataSource.SESSION_CONN_KEY);
	}

	public String toString()
	{
		return getClass().getName() + "[name=" + name + "]";
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Serializable getData()
	{
		return data;
	}

	public void setData(Serializable data)
	{
		this.data = data;
	}

	public Long getConnId() {
		return connId;
	}

	public void setConnId(Long connId) {
		this.connId = connId;
	}
}
