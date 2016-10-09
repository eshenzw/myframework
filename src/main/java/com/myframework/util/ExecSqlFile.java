package com.myframework.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;

/**
 * The Class ExecSqlFile.
 * 
 * @author liujiang
 */
public class ExecSqlFile
{
	/** 数据库驱动 */
	String driver = "";
	/** 数据库地址 */
	String url = "";
	/** 数据库账号 */
	String userId = "";
	/** 数据库密码 */
	String password = "";
	/** 待执行的SQL文件路径集合 */
    List<String> sqlFile  = new ArrayList<String>();

	/**
	 * Instantiates a new exec sql file.
	 */
	public ExecSqlFile()
	{
	}

	/**
	 * <pre>
	 * 初始化参数.
	 * 
	 * @param url       数据库连接地址
	 * @param userId    数据库登录账号
	 * @param password  数据库登录密码
	 * 
	 * </pre>
	 */
	public ExecSqlFile(String url, String userId, String password)
	{
		this.driver = "org.postgresql.Driver";
		this.url = url;
		this.userId = userId;
		this.password = password;
	}

	/**
	 * <pre>
	 * 初始化参数.
	 * 
	 * @param url       数据库连接地址
	 * @param userId    数据库登录账号
	 * @param password  数据库登录密码
     * 
     * </pre>
     */
    public ExecSqlFile(String url,String userId,String password,String driver)
    {
        this.driver = driver;
        this.url = url;
        this.userId = userId;
        this.password = password;
    }

    /**
     * <pre>
     * 初始化参数.
     * 
     * @param url       数据库连接地址
     * @param userId    数据库登录账号
     * @param password  数据库登录密码
	 * @param sqlFile   待执行的SQL文件路径集合
	 * 
	 * </pre>
	 */
	public ExecSqlFile(String url, String userId, String password, ArrayList<String> sqlFile)
	{
		this.driver = "org.postgresql.Driver";
		this.url = url;
		this.userId = userId;
		this.password = password;
		this.sqlFile = sqlFile;
	}

	/**
	 * 执行sql脚本.
	 * 
     * @param path the path SQL文件路径
	 * @return true, if exec sql
	 */
	public boolean execSql(String path)
	{
        return this.execSql(path, driver);
    }

    /**
     * 执行sql脚本.
     * 
     * @param path the path SQL文件路径
     * @return true, if exec sql
     */
    public boolean execSql(String path, String driver)
    {
        return this.execSql(path, driver, false);
    }

    /**
     * 执行sql脚本.
     * 
     * @param path the path SQL文件路径
     * @return true, if exec sql
     */
    public boolean execSql(String path, String driver, boolean autocommit)
    {
        return this.execSql(new File(path), driver, autocommit);
    }
    
    /**
     * 执行sql脚本.
     * 
     * @param path the path SQL文件路径
     * @return true, if exec sql
     */
    public boolean execSql(File file)
    {
        return this.execSql(file, driver, false);
    }
    
    private boolean execSql(File file, String driver, boolean autocommit)
    {
		// 设置数据库参数
		SQLExec sqlexec = new SQLExec();
		sqlexec.setEncoding("UTF-8");
		sqlexec.setDriver(driver);
		sqlexec.setUrl(url);
		sqlexec.setUserid(userId);
		sqlexec.setPassword(password);
        sqlexec.setSrc(file);
		sqlexec.setPrint(true);
		sqlexec.setProject(new Project());
        sqlexec.setAutocommit(autocommit);
        sqlexec.setExpandProperties(false);
        SQLExec.DelimiterType delimiterType = new SQLExec.DelimiterType() ;
        delimiterType.setValue("row");
        sqlexec.setDelimiterType(delimiterType);
		sqlexec.execute();
        return true;
	}


	/**
	 * 执行SQL操作入口.
	 * 
	 * @return true, if successful
	 */
	public boolean execSql()
	{
		boolean flag = true;
		for (String file : sqlFile)
		{
			boolean exec_flag = this.execSql(file);
			if (!exec_flag)
			{
				flag = false;
			}
		}
		return flag;
	}

	/**
     * <pre>
	 * 执行sql脚本.
	 * 
     * @param text       语句
     * @param autocommit 是否自动提交标识
     * @return true      操作成功
     * 
     * </pre>
	 */
    public boolean execSqlText(String text, boolean autocommit)
	{
        return this.execSqlText(text, autocommit, driver);
    }

    /**
     * <pre>
     * 执行sql脚本.
     * 
     * @param text       语句
     * @param autocommit 是否自动提交标识
     * @return true      操作成功
     * 
     * </pre>
     */
    public boolean execSqlText(String text, boolean autocommit, String driver)
    {
		// 设置数据库参数
		SQLExec sqlexec = new SQLExec();
		sqlexec.setDriver(driver);
		sqlexec.setUrl(url);
		sqlexec.setUserid(userId);
		sqlexec.setPassword(password);
		sqlexec.addText(text);
		sqlexec.setPrint(true);
		sqlexec.setProject(new Project());
        sqlexec.setAutocommit(autocommit);
        sqlexec.setExpandProperties(false);
		sqlexec.execute();
        return true;
	}

	/**
	 * The main method.
	 * 
     * @param args the args
	 */
	public static void main(String[] args)
	{
        String url_ = "jdbc:postgresql://127.0.0.1/postgres?useUnicode=true&characterEncoding=UTF-8";
        // ExecSqlFile sql = new ExecSqlFile("org.postgresql.Driver", url_, "postgres", "890524", "", "");
        ExecSqlFile sql = new ExecSqlFile(url_, "postgres", "890524", "org.postgresql.Driver");
        boolean b = sql.execSql("D:\\jj.sql");
        System.out.println("result :" + b);
	}

	public String getDriver()
	{
		return driver;
	}

	public void setDriver(String driver)
	{
		this.driver = driver;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @return the sqlFile
	 */
	public List<String> getSqlFile() {
		return sqlFile;
	}

	/**
	 * @param sqlFile the sqlFile to set
	 */
	public void setSqlFile(List<String> sqlFile) {
		this.sqlFile = sqlFile;
	}

    
}
