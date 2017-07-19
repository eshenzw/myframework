package com.myframework.core.db.multi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by zw
 */
public class DynamicDataSource extends DelegatingDataSource {

    private static Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    @Override
    public void afterPropertiesSet() {
        // do nothing
    }

    @Override
    public DataSource getTargetDataSource() {
        DataSource ds = MutiDataSourceRouter.getCurrentDataSource();

        logger.debug("DynamicDataSource getTargetDataSource：" + ds.toString());

        return ds;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = MutiDataSourceRouter.getCurrentDataSource().getConnection();
        return ConnectionWapper.getInstance(conn);
    }
}
