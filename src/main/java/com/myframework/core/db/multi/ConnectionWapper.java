package com.myframework.core.db.multi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by zw.
 */
public class ConnectionWapper implements InvocationHandler {

    private static final String CLOSE_METHOD = "close";

    private Connection conn = null;

    private ConnectionWapper(Connection conn) {
        this.conn = conn;
    }

    public static Connection getInstance(Connection conn) {
        if (conn == null) {
            return null;
        }

        InvocationHandler handler = new ConnectionWapper(conn);

        return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), conn.getClass().getInterfaces(), handler);

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (CLOSE_METHOD.equals(method.getName())) {
            Object obj = method.invoke(conn, args);

            if (isNeedRemoveConn()) {
                closeConn(conn);
            }

            return obj;
        }

        return method.invoke(conn, args);
    }

    private boolean isNeedRemoveConn() {
        if (DataSourceHolder.isForceClose() && !DataSourceHolder.isTransaction()) {
            return true;
        }
        return false;
    }

    private void closeConn(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
