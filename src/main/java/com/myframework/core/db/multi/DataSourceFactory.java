package com.myframework.core.db.multi;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallFilter;
import com.myframework.constant.Constants;
import com.myframework.core.db.DBInfo;
import com.myframework.util.DesUtils;
import com.myframework.util.PropertiesUtil;
import com.myframework.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw
 */
public class DataSourceFactory {

    private final static Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);

    public static final String MASTER_DB_KEY = "MASTER";
    public static final String DB_UN_KNOWN = "UN_KNOWN";

    public static List<DataSource> initMasterDataSource() {
        List<DataSource> dataSources = new ArrayList<DataSource>();
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(PropertiesUtil.getProp(Constants.JDBC_FILE_PATH, "db.driver"));
        ds.setUrl(PropertiesUtil.getProp(Constants.JDBC_FILE_PATH, "db.url"));
        ds.setUsername(PropertiesUtil.getProp(Constants.JDBC_FILE_PATH, "db.user"));
        DesUtils des = null;// 自定义密钥
        try {
            des = new DesUtils();
            ds.setPassword(des.decrypt(PropertiesUtil.getProp(Constants.JDBC_FILE_PATH, "db.password")));
        } catch (Exception e) {
            System.out.println("初始化主数据库解密失败:" + e.getMessage());
            e.printStackTrace();
        }
        setDruidDataParam(ds);
        // ds.setFilters("stat");
        dataSources.add(ds);
        return dataSources;
    }

    public static List<DataSource> createDataSourceByKey(String dbKey) {

        List<DataSource> dataSources = new ArrayList<DataSource>();
        List<DBInfo> dbInfos = getDBinfoByKey(dbKey);
        for (DBInfo dbInfo : dbInfos) {
            DruidDataSource ds = new DruidDataSource();
            ds.setDriverClassName(dbInfo.getDbDirver());
            ds.setUrl(dbInfo.getDbUrl());
            ds.setUsername(dbInfo.getDbUsername());
            ds.setPassword(dbInfo.getDbPassword());
            setDruidDataParam(ds);
            dataSources.add(ds);
        }
        return dataSources;
    }

    private static List<DBInfo> getDBinfoByKey(String dbKey) {
        List<DBInfo> dbInfos = new ArrayList<DBInfo>();
        DBInfo dbInfo = new DBInfo();
        dbInfo.setDbDirver(PropertiesUtil.getProp(Constants.JDBC_FILE_PATH, String.format("%s.db.driver", dbKey)));
        dbInfo.setDbUrl(PropertiesUtil.getProp(Constants.JDBC_FILE_PATH, String.format("%s.db.url", dbKey)));
        dbInfo.setDbUsername(PropertiesUtil.getProp(Constants.JDBC_FILE_PATH, String.format("%s.db.user", dbKey)));
        DesUtils des = null;
        try {
            des = new DesUtils();
            dbInfo.setDbPassword(des.decrypt(PropertiesUtil.getProp(Constants.JDBC_FILE_PATH, String.format("%s.db.password", dbKey))));
        } catch (Exception e) {
            System.out.println("初始化数据库解密失败:" + e.getMessage());
            e.printStackTrace();
        }
        dbInfos.add(dbInfo);
        return dbInfos;
    }

    private static void setDruidDataParam(DruidDataSource ds) {
        ds.setMaxWait(60000);
        ds.setInitialSize(1);
        ds.setMinIdle(1);
        ds.setMaxActive(20);
        ds.setTimeBetweenEvictionRunsMillis(60000);
        ds.setMinEvictableIdleTimeMillis(300000);
        ds.setValidationQuery("select 1");
        //
        ds.setPoolPreparedStatements(true);
        ds.setMaxPoolPreparedStatementPerConnectionSize(20);
        //
        ds.setTestOnBorrow(true);
        ds.setTestOnReturn(false);
        ds.setTestWhileIdle(false);

        StatFilter statfilter = null;
        try {
            statfilter = SpringContextUtil.getBean(StatFilter.class);
        } catch (Exception e) {
            logger.warn("没有启用druid statFilter.");
        }
        if (statfilter != null) {
            ds.setUseGlobalDataSourceStat(true);
            ds.getProxyFilters().add(statfilter);
        }
        WallFilter wallFilter = null;
        try {
            wallFilter = SpringContextUtil.getBean(WallFilter.class);
        } catch (Exception e1) {
            logger.warn("没有启用druid wallFilter.");
        }
        if (wallFilter != null) {
            ds.getProxyFilters().add(wallFilter);
        }
    }
}
