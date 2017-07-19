package com.myframework.event;

import java.io.Serializable;

import com.myframework.core.db.multi.DataSourceHolder;
import com.myframework.core.filter.RequestFilter;

/***
 * 事件消息体
 *
 * @author zw
 *
 */
public class MyEvent implements Serializable {
    private static final long serialVersionUID = 8256916968264450362L;
    protected String name;
    protected String dbKey;
    protected Serializable data;

    public MyEvent(String name) {
        if (name == null)
            throw new IllegalArgumentException("事件名称不得为空");
        this.name = name;
        this.dbKey = (String) RequestFilter.getSession().getAttribute(DataSourceHolder.SESSION_DB_KEY);
    }

    public MyEvent(String name, Serializable data) {
        if (name == null)
            throw new IllegalArgumentException("事件名称不得为空");
        this.name = name;
        this.data = data;
        this.dbKey = (String) RequestFilter.getSession().getAttribute(DataSourceHolder.SESSION_DB_KEY);
    }

    public String toString() {
        return getClass().getName() + "[name=" + name + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }

    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }
}
