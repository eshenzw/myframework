package com.myframework.db.multi;

import com.alibaba.druid.pool.DruidDataSource;
import com.myframework.constant.Constants;
import com.myframework.util.DesUtils;
import com.myframework.util.PropertiesUtil;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw
 */
public class DataSourceFactory {

    public static final String MASTER_DB_KEY = "MASTER";
    public static final String DB_UN_KNOWN = "UN_KNOWN";

    public static List<DataSource> initMasterDataSource() {
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
        // ds.setFilters("stat");
        List<DataSource> dataSources = new ArrayList<DataSource>();
        dataSources.add(ds);
        return dataSources;
    }
}
