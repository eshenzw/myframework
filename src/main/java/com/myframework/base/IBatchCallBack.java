package com.myframework.base;

import java.sql.SQLException;

/**
 * Created by ZW on 2015/9/5.
 */
public interface IBatchCallBack {
    void doBatchCallBack() throws SQLException;
}
