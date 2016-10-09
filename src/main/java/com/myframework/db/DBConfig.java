package com.myframework.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.myframework.constant.Constants;
import com.myframework.util.DesUtils;
import com.myframework.util.StringUtil;

/**
 * The Class DBConfig.
 * 
 * @author zw*
 */
public final class DBConfig
{
	/** JDBC配置文件路径. */
	public static final String CONFIG_DEFAULT = "db";
	public static final String CONFIG_READ = "readdb";
	public static final String CONFIG_WRITE = "writedb";
	/** 单体实例. */
	private static Map<String, DBConfig> instanceMap = new Hashtable<String, DBConfig>();
	/** 使用数据源作为连接获取方式时，JNDI 的名称. */
	private String jndiName;
	/** jdbc 驱动类. */
	private String jdbcClass;
	/** jdbc 连接url. */
	private String jdbcUrl;
	/** jdbc 连接用户名. */
	private String jdbcUserName;
	/** jdbc 连接密码. */
	private String jdbcPassword;

	/**
	 * 非公开构造函数，读取初始化参数.
	 */
	private DBConfig()
	{
		this.loadConfig(CONFIG_DEFAULT);
	}

	/**
	 * 非公开构造函数，读取初始化参数.
	 * dbName:：CONFIG_READ 读库config   CONFIG_WRITE ：写库config
	 */
	private DBConfig(String dbName)
	{
		this.loadConfig(dbName);
	}

	/**
	 * 单例方法.
	 * 
	 * @return the instance
	 */
	public static synchronized DBConfig getInstance()
	{
		if (!instanceMap.containsKey(CONFIG_DEFAULT))
		{
			instanceMap.put(CONFIG_DEFAULT, new DBConfig());
		}
		return instanceMap.get(CONFIG_DEFAULT);
	}

	/**
	 * 单例方法.
	 *
	 * @return the read
	 */
	public static synchronized DBConfig getInstance(String dbName)
	{
		if (!instanceMap.containsKey(dbName))
		{
			instanceMap.put(dbName, new DBConfig(dbName));
		}
		return instanceMap.get(dbName);
	}

	/**
	 * 读取参数配置.
	 */
	private void loadConfig(String dbName)
	{
		InputStream streamIn = DBConfig.class.getClassLoader().getResourceAsStream(Constants.JDBC_FILE_PATH);
		try
		{
			if (StringUtil.isEmpty(dbName) || CONFIG_DEFAULT.equals(dbName))
			{
				loadConfig(streamIn, CONFIG_DEFAULT);
			}
			else
			{
				loadConfig(streamIn, dbName);
			}
			DesUtils des = new DesUtils();// 自定义密钥
			this.setJdbcPassword(des.decrypt(this.getJdbcPassword()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (streamIn != null)
			{
				try
				{
					streamIn.close();
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * 从流中读取配置.
	 * 
	 * @param streamIn
	 *            the stream in
	 */
	private void loadConfig(InputStream streamIn, String dbName)
	{
		try
		{
			Properties prop = new Properties();
			prop.load(streamIn);
			Map jdbcPM = new Hashtable();
			jdbcPM = prop;
			this.jndiName = propertyConverter(prop.getProperty(String.format("%s.jndiName", dbName)), jdbcPM);
			this.jdbcClass = propertyConverter(prop.getProperty(String.format("%s.driver", dbName)), jdbcPM);
			this.jdbcUrl = propertyConverter(prop.getProperty(String.format("%s.url", dbName)), jdbcPM);
			this.jdbcUserName = propertyConverter(prop.getProperty(String.format("%s.username", dbName)), jdbcPM);
			this.jdbcPassword = propertyConverter(prop.getProperty(String.format("%s.password", dbName)), jdbcPM);
		}
		catch (IOException ex)
		{
			System.err.println("ERROR jdbc.properties file failed. Please Check this file is exist in classes path.");
			ex.printStackTrace();
		}
	}

	/**
	 * 测试方法：主要测试${xxxx}.
	 * 
	 * @return the string
	 */
	private String test()
	{
		Map map = new Hashtable();
		map.put("dept.dept", "开${emp}发部");
		map.put("emp", "张三");
		StringBuffer sb = new StringBuffer();
		String aa = propertyConverter("inset into{emp} dept=${dept.dept}; emp=${emp}--dept=${dept.dept}", map);
		System.out.println(aa);
		return "";
	}

	/**
	 * Test2.
	 * 
	 * @return the string
	 */
	private String test2()
	{
		Map map = new Hashtable();
		map.put("dept.dept", "开${emp}发部");
		map.put("emp", "张三");
		map.put("dept", "deptid");
		map.put("deptid.age", "12");
		StringBuffer sb = new StringBuffer();
		String aa = propertyConverter(
				"inset into{emp} dept=${dept.dept}; 它的年龄是【${${dept}.age}】 emp=${emp}--dept=${dept.dept}", map);
		System.out.println(aa);
		return "";
	}

	/**
	 * 属性转换.
	 * 
	 * @param sequence
	 *            the sequence
	 * @param map
	 *            the map
	 * @return the string
	 */
	private String propertyConverter(String sequence, Map map)
	{
		if (sequence == null)
		{
			return "";
		}
		Pattern p = Pattern.compile("[\\$][{][\\w|.]+[}]"); // 匹配模式
		boolean result = true;
		while (result)
		{
			Matcher m = p.matcher(sequence);
			result = m.find();
			m = p.matcher(sequence);
			result = m.find();
			if (result)
			{
				String name = m.group(0).substring(2, m.group(0).length() - 1);
				// System.out.println(m.group(0));
				String value = "";
				if (map.get(name) != null)
				{
					// 如果replace出现$字符会报错：Illegal group reference
					// 所以需要转义 $ ==> \\$
					value = filterDollarStr((String) map.get(name));
				}
				sequence = m.replaceFirst(value);
			}
		}
		return sequence;
	}

	/**
	 * 转义$ => \\$.
	 * 
	 * @param str
	 *            the str
	 * @return the string
	 */
	private static String filterDollarStr(String str)
	{
		String sReturn = "";
		if (!"".equals(str))
		{
			if (str.indexOf('$', 0) > -1)
			{
				while (str.length() > 0)
				{
					if (str.indexOf('$', 0) > -1)
					{
						sReturn += str.subSequence(0, str.indexOf('$', 0));
						sReturn += "\\$";
						str = str.substring(str.indexOf('$', 0) + 1, str.length());
					}
					else
					{
						sReturn += str;
						str = "";
					}
				}
			}
			else
			{
				sReturn = str;
			}
		}
		return sReturn;
	}

	/**
	 * Gets the jdbc class.
	 * 
	 * @return the jdbc class
	 */
	public String getJdbcClass()
	{
		return jdbcClass;
	}

	/**
	 * Gets the jdbc password.
	 * 
	 * @return the jdbc password
	 */
	public String getJdbcPassword()
	{
		return jdbcPassword;
	}

	/**
	 * Gets the jdbc url.
	 * 
	 * @return the jdbc url
	 */
	public String getJdbcUrl()
	{
		return jdbcUrl;
	}

	/**
	 * Gets the jdbc user name.
	 * 
	 * @return the jdbc user name
	 */
	public String getJdbcUserName()
	{
		return jdbcUserName;
	}

	/**
	 * Gets the jndi name.
	 * 
	 * @return the jndi name
	 */
	public String getJndiName()
	{
		return jndiName;
	}

	/**
	 * Sets the jdbc class.
	 * 
	 * @param jdbcClass
	 *            the new jdbc class
	 */
	public void setJdbcClass(String jdbcClass)
	{
		this.jdbcClass = jdbcClass;
	}

	/**
	 * Sets the jdbc password.
	 * 
	 * @param jdbcPassword
	 *            the new jdbc password
	 */
	public void setJdbcPassword(String jdbcPassword)
	{
		this.jdbcPassword = jdbcPassword;
	}

	/**
	 * Sets the jdbc url.
	 * 
	 * @param jdbcUrl
	 *            the new jdbc url
	 */
	public void setJdbcUrl(String jdbcUrl)
	{
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * Sets the jdbc user name.
	 * 
	 * @param jdbcUserName
	 *            the new jdbc user name
	 */
	public void setJdbcUserName(String jdbcUserName)
	{
		this.jdbcUserName = jdbcUserName;
	}

	/**
	 * Sets the jndi name.
	 * 
	 * @param jndiName
	 *            the new jndi name
	 */
	public void setJndiName(String jndiName)
	{
		this.jndiName = jndiName;
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) throws Exception
	{
		//
		DBConfig jc = new DBConfig(CONFIG_DEFAULT);
		jc.test();
		jc.test2();
		DesUtils des = new DesUtils();// 自定义密钥
		String testDes = "parttime2016";
		System.out.println("加密后的密码：" + des.encrypt(testDes));
		System.out.println("解密后的密码：" + des.decrypt(des.encrypt(testDes)));
		System.out.println("数据库配置:driver=" + jc.getJdbcClass() + "\n url=" + jc.getJdbcUrl() + "\n username="
				+ jc.getJdbcUserName() + "\n password=" + jc.getJdbcPassword());
	}
}
