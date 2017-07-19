package com.myframework.db.multi;

/**
 * Created by zw
 */
public class DataSourceHolder {
    private static final ThreadLocal<String> dbKey = new ThreadLocal<String>();
    private static final ThreadLocal<Boolean> isMaster = new ThreadLocal<Boolean>();

    public static String getDbKey() {
        if (dbKey.get() != null) {
            return dbKey.get();
        } else {
            return DataSourceFactory.MASTER_DB_KEY;
        }
    }

    public static boolean isMaster() {
        if (isMaster.get() != null) {
            return isMaster.get();
        }
        return true;
    }
}
