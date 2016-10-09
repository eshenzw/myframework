package com.myframework.db;

import java.io.Serializable;

/**
 * The Class DBInfo.
 * 
 * @author zw
 */
public class DBInfo implements Serializable
{
	/** 默认数据库ID */
	public static final Long CONNID_DEFAULT = 0L;
	/** 读数据库ID */
	public static final Long CONNID_READ = 1L;
	/** 写数据库ID */
	public static final Long CONNID_WRITE = 2L;
	/** 数据库状态 停用 0 */
	public static final String STATUS_STOP = "0";
	/** 数据库状态 启用 1 */
	public static final String STATUS_RUN = "1";
	/** 数据库状态 禁用 2 */
	public static final String STATUS_FORBIDDEN = "2";

	private Long connId;
	/** 数据库链接名. */
	private String connCode;
	/** 数据库是否使用. */
	private String dbStatus;
	/** 数据库URL. */
	private String dbUrl;
	/** 数据库访问用户名. */
	private String dbUsername;
	/** 数据库访问密码. */
	private String dbPassword;
	/** 数据库驱动. */
	private String dbDirver;
	/** 最大连接数. */
	private int dbMaximumConnectionCount;
	/** 最小连接数. */
	private int dbMinimumConnectionCount;
	/** 空闲回收时间. */
	private int dbHouseKeepingSleepTime;

	public String getDbUrl()
	{
		return dbUrl;
	}

	public void setDbUrl(String dbUrl)
	{
		this.dbUrl = dbUrl;
	}

	public String getDbUsername()
	{
		return dbUsername;
	}

	public void setDbUsername(String dbUsername)
	{
		this.dbUsername = dbUsername;
	}

	public String getDbPassword()
	{
		return dbPassword;
	}

	public void setDbPassword(String dbPassword)
	{
		this.dbPassword = dbPassword;
	}

	public String getDbDirver()
	{
		return dbDirver;
	}

	public void setDbDirver(String dbDirver)
	{
		this.dbDirver = dbDirver;
	}

	public int getDbMaximumConnectionCount()
	{
		return dbMaximumConnectionCount;
	}

	public void setDbMaximumConnectionCount(int dbMaximumConnectionCount)
	{
		this.dbMaximumConnectionCount = dbMaximumConnectionCount;
	}

	public int getDbMinimumConnectionCount()
	{
		return dbMinimumConnectionCount;
	}

	public void setDbMinimumConnectionCount(int dbMinimumConnectionCount)
	{
		this.dbMinimumConnectionCount = dbMinimumConnectionCount;
	}

	public int getDbHouseKeepingSleepTime()
	{
		return dbHouseKeepingSleepTime;
	}

	public void setDbHouseKeepingSleepTime(int dbHouseKeepingSleepTime)
	{
		this.dbHouseKeepingSleepTime = dbHouseKeepingSleepTime;
	}

	public Long getConnId() {
		return connId;
	}

	public void setConnId(Long connId) {
		this.connId = connId;
	}

	public String getConnCode() {
		return connCode;
	}

	public void setConnCode(String connCode) {
		this.connCode = connCode;
	}

	public String getDbStatus() {
		return dbStatus;
	}

	public void setDbStatus(String dbStatus) {
		this.dbStatus = dbStatus;
	}
}
