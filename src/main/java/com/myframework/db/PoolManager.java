package com.myframework.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.logicalcobwebs.proxool.*;
import org.logicalcobwebs.proxool.admin.SnapshotIF;

/**
 * The Class PoolManager.
 * <ul>
 * <li>查看有几个池；</li>
 * <li>查看池的信息；</li>
 * <li>关闭池连接；</li>
 * </ul>
 * 
 * @author zw*
 */
public class PoolManager
{
	/**
	 * 获取有几个池.
	 * 
	 * @return the poolsize 连接池个数
	 */
	public static int getPoolSize()
	{
		int count = 0;
		String[] aliases = getAllPoolName();
		if (aliases != null)
		{
			count = aliases.length;
		}
		return count;
	}

	/**
	 * 获取所有的池的名字.
	 * 
	 * @return the all pool name
	 */
	public static String[] getAllPoolName()
	{
		return ProxoolFacade.getAliases();
	}

	/**
	 * 获取池信息根据池名称.
	 * 
	 * @param poolName
	 *            池名称
	 * @return the pool info
	 */
	public static PoolInfo getPoolInfo(String poolName)
	{
		PoolInfo pi = new PoolInfo();
		try
		{
			ConnectionPoolDefinitionIF cpi = ProxoolFacade.getConnectionPoolDefinition(poolName);
			SnapshotIF sif = ProxoolFacade.getSnapshot(poolName, true);
			List<ConnectionInfo> cpifs = new ArrayList<ConnectionInfo>();
			pi.setDriver(cpi.getDriver());
			pi.setMaxnum(cpi.getMaximumConnectionCount());
			pi.setMinnum(cpi.getMinimumConnectionCount());
			pi.setName(cpi.getAlias());
			pi.setUrl(cpi.getUrl());
			pi.setUsername(cpi.getUser());
			pi.setPassword(cpi.getPassword());
			pi.setActivenum(sif.getConnectionCount());
			pi.setConnectionInfos(cpifs);
			pi.setVersion(Version.getVersion());
			ConnectionInfoIF[] coifs = sif.getConnectionInfos();
			for (ConnectionInfoIF coif : coifs)
			{
				ConnectionInfo cif = new ConnectionInfo();
				cif.setId(coif.getId());
				cif.setPoolName(cpi.getAlias());
				cif.setBorn(coif.getBirthDate());
				cif.setLastActive(new Date(coif.getTimeLastStartActive()));
				long lap = 0;
				if (coif.getTimeLastStopActive() > 0)
				{
					lap = coif.getTimeLastStopActive() - coif.getTimeLastStartActive();
				}
				else if (coif.getTimeLastStartActive() > 0)
				{
					lap = sif.getSnapshotDate().getTime() - coif.getTimeLastStartActive();
				}
				cif.setLap(lap);
				cif.setThread(coif.getRequester());
				cpifs.add(cif);
			}
		}
		catch (ProxoolException e)
		{
			return null;
		}
		return pi;
	}

	/**
	 * 获取所有池的信息.
	 * 
	 * @return the all pool info
	 */
	public static List<PoolInfo> getAllPoolInfo()
	{
		List<PoolInfo> list = new ArrayList<PoolInfo>();
		for (String poolName : getAllPoolName())
		{
			PoolInfo pi = getPoolInfo(poolName);
			if (pi != null)
			{
				list.add(pi);
			}
		}
		return list;
	}

	/**
	 * 清空池内所有连接.
	 * 
	 * @param poolName
	 *            the pool name
	 * @return true, if successful
	 */
	public static boolean killPool(String poolName)
	{
		boolean isKilled = true;
		try
		{
			ProxoolFacade.killAllConnections(poolName, "被管理员终止数据库连接池");
		}
		catch (ProxoolException e)
		{
			isKilled = false;
		}
		return isKilled;
	}

	/**
	 * 关闭这个连接.
	 * 
	 * @param poolName
	 *            the pool name
	 * @param id
	 *            the id
	 * @return true, if successful
	 */
	public static boolean killConnection(String poolName, long id)
	{
		boolean isKilled = true;
		try
		{
			ProxoolFacade.killConnecton(poolName, id, true);
		}
		catch (ProxoolException e)
		{
			isKilled = false;
		}
		return isKilled;
	}
}
