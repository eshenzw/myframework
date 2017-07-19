package com.myframework.core.db;

import java.util.Date;

/**
 * The Class ConnectionInfo.
 * 
 * @author zw*
 */
public class ConnectionInfo
{
	/** 连接编号. */
	private long id;
	/** 池名称. */
	private String poolName;
	/** 连接创建时间. */
	private Date born;
	/** 最后活跃时间. */
	private Date lastActive;
	/** 耗时. */
	private long lap;
	/** 线程使用信息. */
	private String thread;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId()
	{
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id)
	{
		this.id = id;
	}

	/**
	 * Gets the pool name.
	 * 
	 * @return the pool name
	 */
	public String getPoolName()
	{
		return poolName;
	}

	/**
	 * Sets the pool name.
	 * 
	 * @param poolName
	 *            the new pool name
	 */
	public void setPoolName(String poolName)
	{
		this.poolName = poolName;
	}

	/**
	 * Gets the born.
	 * 
	 * @return the born
	 */
	public Date getBorn()
	{
		return born;
	}

	/**
	 * Sets the born.
	 * 
	 * @param born
	 *            the new born
	 */
	public void setBorn(Date born)
	{
		this.born = born;
	}

	/**
	 * Gets the last active.
	 * 
	 * @return the last active
	 */
	public Date getLastActive()
	{
		return lastActive;
	}

	/**
	 * Sets the last active.
	 * 
	 * @param lastActive
	 *            the new last active
	 */
	public void setLastActive(Date lastActive)
	{
		this.lastActive = lastActive;
	}

	/**
	 * Gets the lap.
	 * 
	 * @return the lap
	 */
	public long getLap()
	{
		return lap;
	}

	/**
	 * Sets the lap.
	 * 
	 * @param lap
	 *            the new lap
	 */
	public void setLap(long lap)
	{
		this.lap = lap;
	}

	/**
	 * Gets the thread.
	 * 
	 * @return the thread
	 */
	public String getThread()
	{
		return thread;
	}

	/**
	 * Sets the thread.
	 * 
	 * @param thread
	 *            the new thread
	 */
	public void setThread(String thread)
	{
		this.thread = thread;
	}
}
