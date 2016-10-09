package com.myframework.db;

import com.myframework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @描述：获取数据链接信息接口
 */
public final class ConnHandler
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnHandler.class);

	/**
	 * 私有构造函数
	 */
	private ConnHandler()
	{
		LOGGER.info("初始化链接信息远程接口！");
	}

	/**
	 * 根据链接ID获取数据库信息
	 * 
	 * @param connId
	 *            链接ID
	 * @return 数据库Bean
	 * @throws Exception
	 *             数据库连接异常
	 */
	public static DBInfo getConnDBInfoById(Long connId)
	{
		if (connId == null || DBInfo.CONNID_MAP.containsKey(connId))
		{
			DBConfig dbConfig = null;
			dbConfig = DBConfig.getInstance(DBInfo.CONNID_MAP.get(connId));
			//
			DBInfo dbInfo = new DBInfo();
			dbInfo.setConnId(connId);
			dbInfo.setConnCode(DBInfo.CONNID_MAP.get(connId));
			dbInfo.setDbStatus(DBInfo.STATUS_RUN);
			dbInfo.setDbDirver(dbConfig.getJdbcClass());
			dbInfo.setDbUrl(dbConfig.getJdbcUrl());
			dbInfo.setDbUsername(dbConfig.getJdbcUserName());
			dbInfo.setDbPassword(dbConfig.getJdbcPassword());
			dbInfo.setDbMinimumConnectionCount(50);
			dbInfo.setDbMaximumConnectionCount(150);
			dbInfo.setDbHouseKeepingSleepTime(5 * 60 * 1000);
			return dbInfo;
		}
		return null;
	}

	public static final int DATASOURCE_SUCCESS = 0;
	public static final int DATASOURCE_FAIL = 1;
	public static final int DATASOURCE_CONN_NULL = 101;
	public static final int DATASOURCE_CONN_STOPED = 102;
	public static final int DATASOURCE_SEVER_ERROR = 103;

	/**
	 * 切换链接数据源
	 * 
	 * @param dbInfo
	 * @return
	 */
	public static int switchConnection(DBInfo dbInfo)
	{
		if (dbInfo == null)
		{
			LOGGER.info("数据源连接切换失败，链接信息为空！");
			return DATASOURCE_CONN_NULL;
		}

		try
		{
			if (RegisterConnDatasource.getConnDatasource(dbInfo))
			{
				return DATASOURCE_SUCCESS;
			}
			else
			{
				if (dbInfo.STATUS_STOP.equals(dbInfo.getDbStatus())
						|| dbInfo.STATUS_FORBIDDEN.equals(dbInfo.getDbStatus()) || "3".equals(dbInfo.getDbStatus()))
				{
					LOGGER.info("企业[{}]数据源连接切换失败，已经销户或停用！", dbInfo.getConnCode());
					return DATASOURCE_CONN_STOPED;
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.warn("链接[{}]数据源连接切换失败，原因：{}", new Object[]
			{
				dbInfo.getConnCode(), e.getMessage()
			});
			return DATASOURCE_SEVER_ERROR;
		}
		return DATASOURCE_FAIL;
	}

	/**
	 * 切换数据源连接
	 *
	 * * @param connId
	 * @return 数据源切换成功与否 true/false
	 */
	public static int switchConnection(Long connId)
	{
		if (connId == null || connId < DBInfo.CONNID_MIN)
		{
			LOGGER.info("数据源连接切换失败，链接信息为空！");
			return DATASOURCE_CONN_NULL;
		}

		try
		{
			if (RegisterConnDatasource.getConnDatasource(connId))
			{
				return DATASOURCE_SUCCESS;
			}
			else
			{
				DBInfo dbInfo = ConnHandler.getConnDBInfoById(connId);
				if (dbInfo.STATUS_STOP.equals(dbInfo.getDbStatus())
						|| dbInfo.STATUS_FORBIDDEN.equals(dbInfo.getDbStatus()) || "3".equals(dbInfo.getDbStatus()))
				{
					LOGGER.info("企业[{}]数据源连接切换失败，已经销户或停用！", dbInfo.getConnCode());
					return DATASOURCE_CONN_STOPED;
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.warn("链接[{}]数据源连接切换失败，原因：{}", new Object[]
			{
				connId, e.getMessage()
			});
			return DATASOURCE_SEVER_ERROR;
		}
		return DATASOURCE_FAIL;
	}

	/**
	 * 切换数据源连接
	 *
	 * * @param dbName
	 * @return 数据源切换成功与否 true/false
	 */
	public static int switchConnection(String dbName)
	{
		if (StringUtil.isEmpty(dbName) || !DBInfo.CONNID_MAP.containsValue(dbName))
		{
			LOGGER.warn("链接[{}]数据源连接切换失败，原因：{}", new Object[]
			{
				dbName, "配置文件没有配置该数据源！"
			});
			return DATASOURCE_CONN_NULL;
		}
		Iterator it = DBInfo.CONNID_MAP.entrySet().iterator();
		Long connId = null;
		while (it.hasNext())
		{
			Map.Entry entry = (Map.Entry) it.next();
			if (entry.getValue().equals(dbName))
			{
				connId = (Long) entry.getKey();
			}
		}
		return switchConnection(connId);
	}
}
