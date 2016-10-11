package com.myframework.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.myframework.filter.RequestFilter;

/**
 * 动态数据源实现类
 * 
 * @author zw
 */
public class DynamicDataSource extends AbstractRoutingDataSource
{
	protected static Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);
	/** 数据源session：key */
	public static final String SESSION_CONN_KEY = "conn_key";
	/** 数据源缓存 */
	private static final Map<Long, DataSource> connDataSources = new ConcurrentHashMap<Long, DataSource>();
	/** 用于存放当前线程的连接数据源信息 适用于没有request对象的后台程序切换数据源 */
	private static final ThreadLocal<Long> currentConnId = new ThreadLocal<Long>();
	private static final Map<Long, Boolean> dss = new ConcurrentHashMap<Long, Boolean>();

	public static boolean isSourceSwitch(Long key)
	{
		Boolean value = dss.get(key);
		if (value == null)
		{
			return false;
		}
		return true;
	}

	public static void setSourceSwitch(Long key)
	{
		dss.put(key, true);
	}

	public static void clearSourceSwitch(Long key)
	{
		dss.remove(key);
	}

	/**
	 * 数据源是否已创建在缓存中
	 * 
	 * @param connId
	 * @return
	 */
	public static boolean isDataSourceExist(Long connId)
	{
		// logger.debug("isDataSourceExist 数据源ID:{} ", connId);
		return connDataSources.containsKey(connId);
	}

	/**
	 * 添加一个数据源ID数据源到缓存中
	 * 
	 * @param connId
	 * @param ds
	 */
	public static void addDataSource(Long connId, DataSource ds)
	{
		logger.info("addDataSource 数据源ID:{}  ", connId);
		connDataSources.put(connId, ds);
	}

	/**
	 * 从缓存中移除数据源(对于已注销的数据源ID)
	 * 
	 * @param connId
	 */
	public static void removeDataSource(Long connId)
	{
		logger.info("removeDataSource 数据源ID:{}  ", connId);
		DataSource ds = connDataSources.remove(connId);
		if (ds != null)
		{
			try
			{
				ds.getConnection().close();
			}
			catch (Exception e)
			{
				logger.warn("关闭数据库连接出错：ConnId:{}", connId, e);
			}
		}
	}

	/**
	 * 从缓存中获取一个数据
	 * 
	 * @param connId
	 * @return
	 */
	public static DataSource getDataSource(Long connId)
	{
		return connDataSources.get(connId);
	}

	/**
	 * 获取数据连接
	 */
	@Override
	public Connection getConnection() throws SQLException
	{

		// 获取数据源IDID
		Long connId = (Long) RequestFilter.getSession().getAttribute(SESSION_CONN_KEY);
		if (connId == null && currentConnId != null)
		{
			connId = currentConnId.get();
			if (connId == null)
			{
				connId = DBInfo.CONNID_MIN+DBConfig.DbEnum.DEFAULT.getIndex();
				RegisterConnDatasource.getConnDatasource(connId);
			}
		}
		// 根据数据源IDID取得数据源
		Connection conn = null;
		if (connId != null && connId >= DBInfo.CONNID_MIN)
		{
			DataSource ds = connDataSources.get(connId);
			if (ds != null)
			{
				conn = ds.getConnection();
			}
			else
			{
				logger.warn("动态数据源缓存中未查到数据源ID数据源,数据源ID{}", connId);
				ds = RegisterConnDatasource.createConnDatasource(connId);
				// 放到缓存
				if (ds != null)
				{
					logger.info("已创建数据源ID数据源,数据源ID{}", connId);
					connDataSources.put(connId, ds);
					conn = ds.getConnection();
				}
			}
		}
		else
		{
			logger.warn("动态数据源无法获得当前数据源IDID！");

		}
		return conn;
	}

	/**
	 * 已重写父类的getConnection()方法 此方法已不会被调用
	 */
	@Override
	protected Object determineCurrentLookupKey()
	{
		logger.error("determineCurrentLookupKey");
		return null;
	}

	/***************************************************************************
	 * 设置当前的数据源ID的ID （适用于没有request对象的后台程序切换数据源）
	 * 
	 * @param connId
	 */
	public static void setCurConn(Long connId)
	{

		// logger.debug("设置数据源IDID到线程中,threadLocal:{},thread:{},数据源ID{}", new
		// Object[]
		// {
		// currentConnId, Thread.currentThread().getId(), connId
		// });
		currentConnId.set(connId);
	}

	public static Long getCurConn()
	{
		return currentConnId.get();
	}
}
