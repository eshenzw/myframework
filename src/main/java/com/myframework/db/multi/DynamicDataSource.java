package com.myframework.db.multi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.sql.DataSource;

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
}
