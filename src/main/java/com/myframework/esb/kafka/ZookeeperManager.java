package com.myframework.esb.kafka;

import java.io.IOException;

import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myframework.constant.Constants;
import com.myframework.util.PropertiesUtil;
import com.myframework.util.StringUtil;

public class ZookeeperManager
{
	private static Logger logger = LoggerFactory.getLogger(ZookeeperManager.class);
	private static ZooKeeper zk = null;
	private static String zookeeperHost = PropertiesUtil.getProp(Constants.SYSTEM_FILE_PATH, "zookeeper.server.hosts",
			"172.31.120.10:2181");

	/**
	 * 初始化Zookeeper
	 */
	public static void init()
	{
		if (zk == null)
		{
			try
			{
				zk = new ZooKeeper(zookeeperHost, 10000, new Watcher()
				{
					// 监控所有被触发的事件
					public void process(WatchedEvent event)
					{
						logger.info("Zookeeper Start!");
					}
				});
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * CreateMode: PERSISTENT (持续的，相对于EPHEMERAL，不会随着client的断开而消失)
	 * PERSISTENT_SEQUENTIAL（持久的且带顺序的） EPHEMERAL (短暂的，生命周期依赖于client session)
	 * EPHEMERAL_SEQUENTIAL (短暂的，带顺序的)
	 */
	private static void create(ZooKeeper zk, String path, String data) throws Exception
	{
		try
		{
			String[] idx = path.split("/");
			StringBuffer dirs = new StringBuffer();
			for (int i = 0; i < idx.length; i++)
			{
				if (!StringUtil.isEmpty(idx[i]))
				{
					dirs.append("/").append(idx[i]);
					if (zk.exists(dirs.toString(), false) == null)
					{
						zk.create(dirs.toString(), StringUtil.stringToByte(data), Ids.OPEN_ACL_UNSAFE,
								CreateMode.PERSISTENT);
					}
				}
			}
		}
		catch (KeeperException e)
		{
			closeZookeeper();
			logger.warn("Zookeeper 连接异常: ", e);
		}
	}

	private static void delete(ZooKeeper zk, String path) throws Exception
	{
		try
		{
			// 删除整个子目录 -1代表version版本号，-1是删除所有版本
			zk.delete(path, -1);
			// System.out.println(zk.getChildren(path ,true));
		}
		catch (KeeperException e)
		{
			closeZookeeper();
			logger.warn("Zookeeper 连接异常: ", e);
		}
	}

	/**
	 * 保存数据到Zookeeper节点
	 * 
	 * @param path
	 *            节点目录
	 * @param data
	 *            数据
	 */
	public static void save(String path, String data) throws Exception
	{
		try
		{
			init();
			if (zk.exists(path, false) == null)
			{
				create(zk, path, data);
			}

			zk.setData(path, StringUtil.stringToByte(data), -1);
		}
		catch (KeeperException e)
		{
			closeZookeeper();
			logger.warn("Zookeeper 连接异常: ", e);
		}
	}

	/**
	 * 从Zookeeper读取数据
	 * 
	 * @param path
	 *            zk路径
	 * @return data
	 */
	public static String read(String path) throws Exception
	{
		try
		{
			init();
			return StringUtil.byteToString(zk.getData(path, false, null));
		}
		catch (KeeperException e)
		{
			closeZookeeper();
			logger.warn("Zookeeper 连接异常: ", e);
		}
		return null;
	}

	/**
	 * 关闭Zookeeper节点
	 */
	public static void closeZookeeper()
	{
		try
		{
			zk.close();
			zk = null;
		}
		catch (InterruptedException e)
		{
			logger.warn("Zookeeper 中断异常: {}", e.getMessage());
		}

	}
}
