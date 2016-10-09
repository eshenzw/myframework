package com.myframework.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 读取配置文件工具类
 * 
 * @DateTime: 2011-3-4
 * @author author
 * @version 1.0
 */
public final class PropertiesUtil
{
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	/**
	 * 当前加载的配置文件属性映射表
	 */
	private Properties properties;
	/**
	 * 已加载的文件属性列表
	 */
	public static Map<String, Properties> mapProps = new HashMap<String, Properties>();

	/***************************************************************************
	 * 读取配置文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @param key
	 *            属性名称
	 * @return 属性值
	 */
	public static String getProp(String fileName, String key)
	{
		Properties props = mapProps.get(fileName);
		if (props == null)
		{
			props = loadProperties(fileName);
			if (!props.isEmpty())
			{
				mapProps.put(fileName, props);
			}
		}
		return props.getProperty(key);
	}

	public static String getProp(String fileName, String key, String defaultValue)
	{
		Properties props = mapProps.get(fileName);
		if (props == null)
		{
			props = loadProperties(fileName);
			if (!props.isEmpty())
			{
				mapProps.put(fileName, props);
			}
		}
		return props.getProperty(key, defaultValue);
	}

	/**
	 * 设置属性值
	 * 
	 * @param fileName
	 *            文件名
	 * @param props
	 *            配置文件
	 * @return 更新结果
	 */
	public static boolean saveProperties(String fileName, Properties props)
	{
		mapProps.put(fileName, props);
		return write(fileName, props);
	}

	/**
	 * 重新加载文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件对象
	 */
	public static Properties reloadProperties(String fileName)
	{
		Properties props = read(fileName);
		mapProps.put(fileName, props);
		return props;
	}

	/**
	 * 加载配置文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return 配置文件
	 */
	public static Properties loadProperties(String fileName)
	{
		Properties props = mapProps.get(fileName);
		if (props == null)
		{
			props = readAsStream(fileName);
			if (!props.isEmpty())
			{
				mapProps.put(fileName, props);
			}
		}
		return props;
	}

	/**
	 * 读取配置文件信息到虚拟机
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件对象
	 */
	private static Properties readAsStream(String fileName)
	{
		logger.info("加载配置文件：{}", fileName);
		Properties properties = new Properties();
		InputStream ins = null;
		try
		{
			ins = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
			// properties.load(ins);
			properties.load(new InputStreamReader(ins, "UTF-8"));
		}
		catch (FileNotFoundException e)
		{
			logger.error("配置文件 {} 找不到！", fileName, e);
		}
		catch (IOException e)
		{
			logger.error("读取配置文件 {} 错误！", fileName, e);
		}
		catch (Exception e)
		{
			logger.error("读取配置文件 {} 失败，原因：{}", fileName, e);
		}
		finally
		{
			IOUtils.closeQuietly(ins);
		}
		return properties;
	}

	/**
	 * 动态加载配置文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return 新加载的文件
	 */
	private static Properties read(String fileName)
	{
		logger.info("动态加载配置文件：{}", fileName);
		Properties properties = new Properties();
		InputStream ins = null;
		try
		{
			String path = PropertiesUtil.class.getClassLoader().getResource(fileName).getPath();
			ins = new FileInputStream(path);
			// properties.load(ins);
			properties.load(new InputStreamReader(ins, "UTF-8"));
		}
		catch (FileNotFoundException e)
		{
			logger.error("配置文件 {} 找不到！", fileName, e);
		}
		catch (IOException e)
		{
			logger.error("读取配置文件 {} 错误！", fileName, e);
		}
		catch (Exception e)
		{
			logger.error("读取配置文件 {} 失败，原因：{}", fileName, e);
		}
		finally
		{
			IOUtils.closeQuietly(ins);
		}
		return properties;
	}

	private static boolean write(String fileName, Properties props)
	{
		OutputStream fos = null;
		try
		{
			String path = PropertiesUtil.class.getClassLoader().getResource(fileName).getPath();
			fos = new FileOutputStream(path);
			props.store(fos, "Save File ");
			return true;
		}
		catch (Exception e)
		{
			logger.error("更新配置文件{}失败", fileName, e);
		}
		finally
		{
			try
			{
				fos.close();
			}
			catch (IOException e)
			{
				logger.error("关闭配置文件{}失败，", fileName, e);
			}
		}
		return false;
	}

	/**
	 * 测试方法
	 * 
	 * @param args
	 *            参数
	 */
	public static void main(String[] args)
	{
		String key = PropertiesUtil.getInstance("gisConfig.properties").getValue("xloc_address");
		System.out.println(key);
	}

	/**
	 * 根据文件名构造配置文件对象
	 * 
	 * @param fileName
	 *            文件名
	 */
	private PropertiesUtil(String fileName)
	{
		properties = loadProperties(fileName);
		if (properties == null)
		{
			properties = new Properties();
		}
	}

	/**
	 * 根据文件名获取文件对象
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件对象实例
	 */
	public static PropertiesUtil getInstance(String fileName)
	{
		return new PropertiesUtil(fileName);
	}

	/**
	 * 读取文件对象关键字的值
	 * 
	 * @param key
	 *            属性名称
	 * @return string 属性值
	 */
	public String getValue(String key)
	{
		if (properties.containsKey(key))
		{
			String value = properties.getProperty(key);
			return value;
		}
		else
		{
			return "";
		}
	}

	/**
	 * 清除文件对象值
	 */
	public void clear()
	{
		properties.clear();
	}

	public Properties getProperties()
	{
		return properties;
	}

	public void setProperties(Properties properties)
	{
		this.properties = properties;
	}
}
