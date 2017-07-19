package com.myframework.core.db.multi;

import com.myframework.core.filter.RequestFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by zw
 */
public class DataSourceHolder {
    private static final String SESSION_DB_KEY = "SESSION_DB_KEY";
    private static final ThreadLocal<String> dbKeyLocal = new ThreadLocal<String>();
    private static final ThreadLocal<Boolean> isMasteLocal = new ThreadLocal<Boolean>();

    public static String getDbKey() {
        String dbKey = null;
        HttpServletRequest request = RequestFilter.getRequest();
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                dbKey = (String) session.getAttribute(SESSION_DB_KEY);
            }
        }
        if (dbKey == null && dbKeyLocal.get() != null) {
            dbKey = dbKeyLocal.get();
        }
        if (dbKey == null) {
            dbKey = DataSourceFactory.MASTER_DB_KEY;
        }
        return dbKey;
    }

    public static boolean isMaster() {
        if (isMasteLocal.get() != null) {
            return isMasteLocal.get();
        }
        return true;
    }
}
