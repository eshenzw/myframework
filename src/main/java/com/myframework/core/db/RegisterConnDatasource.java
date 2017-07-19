package com.myframework.core.db;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myframework.core.filter.RequestFilter;
import com.myframework.util.StringUtil;

/**
 * 
 * @描述：数据源注册
 */
public class RegisterConnDatasource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterConnDatasource.class);

	/**
	 * 根据链接切换数据源,当数据源池中已经存在时直接获取，不存在时创建新数据源
	 * 
	 * @param connId
	 *            链接ID
	 * @return true/false 是否切换数据源
	 */
	public static boolean getConnDatasource(Long connId)
	{
		try
		{
			// 设置当前链接数据源标志到session中
			HttpServletRequest request = RequestFilter.getRequest();
			if (request != null)
			{
				HttpSession session = request.getSession(false);
				if (session != null)
				{
					session.setAttribute(DynamicDataSource.SESSION_CONN_KEY, connId);
				}
				else
				{
					LOGGER.info("session is null, 设置链接信息到线程环境中");
					DynamicDataSource.setCurConn(connId);
				}
			}
			else
			{
				LOGGER.info("request is null, 设置链接信息到线程环境中");
				DynamicDataSource.setCurConn(connId);
			}

			// 如果动态数据源中已经有本链接的数据源则不需要设置
			if (DynamicDataSource.isDataSourceExist(connId))
			{
				if (DynamicDataSource.isSourceSwitch(connId))
				{
					existsConnectionDataSource(ConnHandler.getConnDBInfoById(connId));
				}
			}
			else
			{
				LOGGER.debug("没找到链接[{}]连接信息缓存，准备创建新的连接！", connId);
				DataSource ds = createConnDatasource(connId);
				if (ds == null)
				{
					return false;
				}
				else
				{
					DynamicDataSource.addDataSource(connId, ds);
				}
			}
			return true;
		}
		catch (Exception e)
		{
			LOGGER.error("根据链接ID{}获取数据源失败，原因：{}", new Object[]
			{
				connId, e.getMessage()
			}, e);
			return false;
		}
	}

	/**
	 * 根据链接信息切换数据源，当数据源池中已经存在时比较连接池是否有变化有变化时创建新连接池并覆盖原来的连接，不存在时创建新数据源
	 * 
	 * @param dbInfo
	 *            链接信息
	 * @return true/false 数据源是否切换
	 */
	public static boolean getConnDatasource(DBInfo dbInfo)
	{
		try
		{
			Long connId = dbInfo.getConnId();
			// 设置当前链接数据源标志到session中
			HttpSession session = RequestFilter.getRequest().getSession(false);
			if (session != null)
			{
				LOGGER.info("设置链接连接信息到请求Session中:{}", dbInfo.getConnId());
				session.setAttribute(DynamicDataSource.SESSION_CONN_KEY, connId);
			}
			else
			{
				LOGGER.info("设置链接连接信息到线程环境中:{}", dbInfo.getConnId());
				DynamicDataSource.setCurConn(connId);
			}
			LOGGER.info("[{}]ddsexists:{}", connId, DynamicDataSource.isDataSourceExist(connId));
			// 如果动态数据源中已经有本链接的数据源则不需要设置
			if (DynamicDataSource.isDataSourceExist(connId))
			{
				existsConnectionDataSource(dbInfo);
			}
			else
			{
				LOGGER.debug("没找到链接[{}]连接信息缓存，准备创建新的连接！", connId);
				DataSource ds = createConnDatasource(dbInfo);
				if (ds == null)
				{
					return false;
				}
				else
				{
					DynamicDataSource.addDataSource(connId, ds);
				}
			}
			return true;
		}
		catch (Exception e)
		{
			LOGGER.error("根据链接信息ID{}获取数据源失败，原因：{}", new Object[]
			{
				dbInfo.getConnId(), e.getMessage()
			}, e);
			return false;
		}
	}

	/**
	 * 链接数据源缓存池中不存在时创建数据源，存在是比较连接是否有变化，有变化时切换数据源要新的数据源配置
	 * 
	 * @param dbInfo
	 *            链接信息
	 * @since 2014-12-23 By sunfg
	 */
	private static void existsConnectionDataSource(DBInfo dbInfo)
	{
		Long connId = dbInfo.getConnId();

		ProxoolDataSource ds = (ProxoolDataSource) DynamicDataSource.getDataSource(connId);
		int newMax = dbInfo.getDbMaximumConnectionCount();

		String newUrl = dbInfo.getDbUrl();
		String oldUrl = ds.getDriverUrl();
		LOGGER.info("[{}]oldmaxconn:{}, newmaxconn:{},oldurl:{},newurl:{}", new Object[]
		{
			connId, ds.getMaximumConnectionCount(), newMax, oldUrl, newUrl
		});
		if (DynamicDataSource.isSourceSwitch(connId) || newMax > ds.getMaximumConnectionCount()
				|| !oldUrl.equals(newUrl))
		{
			// 创建一个新的数据源替代原来的数据源
			LOGGER.warn("DBConnectReset,conn:{},old:{},new:{},", new Object[]
			{
				connId, ds.getMaximumConnectionCount(), newMax
			});
			String strOldAlias = ds.getAlias();
			ProxoolDataSource newDs = createConnDatasource(strOldAlias + StringUtil.random(2), dbInfo);
			DynamicDataSource.addDataSource(connId, newDs);
			DynamicDataSource.clearSourceSwitch(connId);
		}
	}

	/**
	 * 根据链接ID创建数据源连接池
	 * 
	 * @param connId
	 *            链接ID
	 * @return 数据源对象
	 */
	public static ProxoolDataSource createConnDatasource(Long connId)
	{
		try
		{
			return createConnDatasource(ConnHandler.getConnDBInfoById(connId));
		}
		catch (Exception e)
		{
			LOGGER.warn("获取链接{}数据源失败,原因：{}", new Object[]
			{
				connId, e.getMessage()
			});
			return null;
		}
	}

	/**
	 * 根据链接信息创建数据源
	 * 
	 * @param dbInfo
	 *            链接信息
	 * @return 数据源对象
	 */
	public static ProxoolDataSource createConnDatasource(DBInfo dbInfo)
	{
		if (dbInfo == null)
		{
			LOGGER.warn("创建Proxool数据源失败，原因：链接信息为空！");
			return null;
		}
		else
		{
			// 根据数据源信息切换数据库
			Long connId = dbInfo.getConnId();
			if (connId == null)
			{
				LOGGER.warn("创建Proxool数据源失败，原因：链接标识为空，无法设置proxool数据源别名！");
				return null;
			}
			return createConnDatasource(String.valueOf(connId), dbInfo);
		}
	}

	/**
	 * 获取当前的链接数据源连接
	 * 
	 * @param connId
	 *            链接ID
	 * @return 数据源
	 * @deprecated
	 */
	@Deprecated
	public static DataSource getDatasource(Long connId)
	{
		return createConnDatasource(connId);
	}

	/**
	 * 根据链接ID和链接DB信息创建数据源
	 * 
	 * @param alias
	 *            数据库连接别名
	 * @param dbInfo
	 *            链接数据库信息
	 * @return 数据源
	 */
	private static ProxoolDataSource createConnDatasource(String alias, DBInfo dbInfo)
	{
		ProxoolDataSource pds = null;
		if (dbInfo == null)
		{
			LOGGER.warn("创建Proxool数据源失败，原因：链接数据库配置信息为空！");
			return null;
		}
		else
		{
			// 根据数据源信息切换数据库
			pds = new ProxoolDataSource();
			pds.setAlias(alias);
			pds.setDriverUrl(dbInfo.getDbUrl());
			pds.setDriver(dbInfo.getDbDirver());
			pds.setUser(dbInfo.getDbUsername());
			pds.setPassword(dbInfo.getDbPassword());
			pds.setMinimumConnectionCount(dbInfo.getDbMinimumConnectionCount());
			pds.setMaximumConnectionCount(dbInfo.getDbMaximumConnectionCount());
			pds.setHouseKeepingSleepTime(1000 * 30);
			// 同一时刻允许申请最大连接数
			pds.setSimultaneousBuildThrottle(50);
			// 增加数据库连接重试机制
			pds.setTestBeforeUse(true);
			pds.setHouseKeepingTestSql("select 1");
			// 空闲数据库连接断开的时间使用数据库的sleep_keep_time
			pds.setMaximumConnectionLifetime(dbInfo.getDbHouseKeepingSleepTime());
		}
		return pds;
	}
}
