package com.myframework.db;

import java.util.List;

/**
 * The Class PoolInfo.
 * 
 * @author zw*
 */
public class PoolInfo
{
	/** 连接池版本. */
	private String version;
	/** 连接池名称. */
	private String name;
	/** 连接池URL. */
	private String url;
	/** 连接池驱动. */
	private String driver;
	/** 用户名. */
	private String username;
	/** 密码. */
	private String password;
	/** 池最大连接数. */
	private int maxnum;
	/** 池最小连接数. */
	private int minnum;
	/** 池内活跃连接数. */
	private long activenum;
	/** 池内的所有活跃连接信息. */
	private List<ConnectionInfo> connectionInfos;

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the new version
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Gets the url.
	 * 
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * Sets the url.
	 * 
	 * @param url
	 *            the new url
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

	/**
	 * Gets the driver.
	 * 
	 * @return the driver
	 */
	public String getDriver()
	{
		return driver;
	}

	/**
	 * Sets the driver.
	 * 
	 * @param driver
	 *            the new driver
	 */
	public void setDriver(String driver)
	{
		this.driver = driver;
	}

	/**
	 * Gets the maxnum.
	 * 
	 * @return the maxnum
	 */
	public int getMaxnum()
	{
		return maxnum;
	}

	/**
	 * Sets the maxnum.
	 * 
	 * @param maxnum
	 *            the new maxnum
	 */
	public void setMaxnum(int maxnum)
	{
		this.maxnum = maxnum;
	}

	/**
	 * Gets the minnum.
	 * 
	 * @return the minnum
	 */
	public int getMinnum()
	{
		return minnum;
	}

	/**
	 * Sets the minnum.
	 * 
	 * @param minnum
	 *            the new minnum
	 */
	public void setMinnum(int minnum)
	{
		this.minnum = minnum;
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            the new username
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * Gets the activenum.
	 * 
	 * @return the activenum
	 */
	public long getActivenum()
	{
		return activenum;
	}

	/**
	 * Sets the activenum.
	 * 
	 * @param activenum
	 *            the new activenum
	 */
	public void setActivenum(long activenum)
	{
		this.activenum = activenum;
	}

	/**
	 * Gets the connection infos.
	 * 
	 * @return the connection infos
	 */
	public List<ConnectionInfo> getConnectionInfos()
	{
		return connectionInfos;
	}

	/**
	 * Sets the connection infos.
	 * 
	 * @param connectionInfos
	 *            the new connection infos
	 */
	public void setConnectionInfos(List<ConnectionInfo> connectionInfos)
	{
		this.connectionInfos = connectionInfos;
	}
}
