package com.myframework.db.multi;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zw
 */
public class MutiDataSourceRouter {


    private static Map<String, List<DataSource>> mutiDatasourceMap = new HashMap<String, List<DataSource>>();

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
        return mutiDatasourceMap.get(dbKey);
    }

    public static List<DataSource> removeDataSourcesByKey(String dbKey) {
        return mutiDatasourceMap.remove(dbKey);
    }
}

