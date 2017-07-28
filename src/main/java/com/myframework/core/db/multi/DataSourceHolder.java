package com.myframework.core.db.multi;

import com.myframework.core.filter.RequestFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by zw
 */
public class DataSourceHolder {
    public static final String SESSION_DB_KEY = "SESSION_DB_KEY";
    private static final ThreadLocal<String> dbKeyLocal = new ThreadLocal<String>();
    private static final ThreadLocal<Boolean> isMasteLocal = new ThreadLocal<Boolean>();
    private static final ThreadLocal<Boolean> isForceCloseLocal = new ThreadLocal<Boolean>();
    private static final ThreadLocal<String> forceDbKeyLocal = new ThreadLocal<String>();
    private static final ThreadLocal<Boolean> transactionLocal = new ThreadLocal<Boolean>();

    public static String getDbKey() {
        String dbKey = null;
        if (dbKey == null && forceDbKeyLocal.get() != null) {
            dbKey = forceDbKeyLocal.get();
        } else {
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
        }
        return dbKey;
    }

    public static boolean isMaster() {
        if (isMasteLocal.get() != null) {
            return isMasteLocal.get();
        }
        return true;
    }

    /**
     *
     */
    public static void clearThreadLocal() {
        dbKeyLocal.remove();
        isMasteLocal.remove();
        isForceCloseLocal.remove();
        forceDbKeyLocal.remove();
        transactionLocal.remove();
    }

    public static Boolean isForceClose() {
        return isForceCloseLocal.get() == null ? false : isForceCloseLocal.get();
    }

    public static Boolean isTransaction() {
        return transactionLocal.get() == null ? false : transactionLocal.get();
    }

    public static void setDbKey(String dbKey) {
        dbKeyLocal.set(dbKey);
    }

    public static void setMaster(Boolean flag) {
        isMasteLocal.set(flag);
    }

    public static void setForceClose(Boolean flag) {
        isForceCloseLocal.set(flag);
    }

    public static void setForceDbKey(String dbKey) {
        forceDbKeyLocal.set(dbKey);
    }

    public static void setTransaction(Boolean flag) {
        transactionLocal.set(flag);
    }

    public static void removeDbKey() {
        dbKeyLocal.remove();
    }

    public static void removeMaster() {
        isMasteLocal.remove();
    }

    public static void removeForceClose() {
        isForceCloseLocal.remove();
    }

    public static void removeForceDbKey() {
        forceDbKeyLocal.remove();
    }

    public static void removeTransaction() {
        transactionLocal.remove();
    }
}
