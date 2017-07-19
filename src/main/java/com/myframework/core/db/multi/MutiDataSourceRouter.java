package com.myframework.core.db.multi;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zw
 */
public class MutiDataSourceRouter {

    private static Logger logger = LoggerFactory.getLogger(MutiDataSourceRouter.class);

    private static Map<String, List<DataSource>> mutiDatasourceMap = new ConcurrentHashMap<String, List<DataSource>>();

    public static DataSource getCurrentDataSource() {
        List<DataSource> targetDataSources = getAllDataSources(DataSourceHolder.getDbKey());
        boolean isMaster = DataSourceHolder.isMaster();
        //master第一个
        if (isMaster) {
            return targetDataSources.get(0);
        } else {
            int size = targetDataSources.size();
            if (size == 1) {
                return targetDataSources.get(0);
            }
            int randomIndex = (int) (Math.random() * (size - 1)) + 1;
            return targetDataSources.get(randomIndex);
        }
    }

    public static List<DataSource> getAllDataSources(String dbKey) {
        if (mutiDatasourceMap.size() == 0) {
            //初始化所有数据源
            List<DataSource> dss = DataSourceFactory.initMasterDataSource();
            mutiDatasourceMap.put(DataSourceFactory.MASTER_DB_KEY, dss);
        }
        if (!mutiDatasourceMap.containsKey(dbKey)) {
            //初始化其他db连接
            List<DataSource> dss = DataSourceFactory.createDataSourceByKey(dbKey);
            mutiDatasourceMap.put(dbKey, dss);
        }
        return mutiDatasourceMap.get(dbKey);
    }

    public static List<DataSource> removeDataSourcesByKey(String dbKey) {
        List<DataSource> dss = mutiDatasourceMap.get(dbKey);
        for (DataSource ds : dss) {
            if (ds instanceof DruidDataSource) {
                ((DruidDataSource) ds).close();
            } else {
                try {
                    ds.getConnection().close();
                } catch (SQLException e) {
                    logger.debug("清除数据源失败！", e);
                    e.printStackTrace();
                }
            }
        }
        return mutiDatasourceMap.remove(dbKey);
    }
}

