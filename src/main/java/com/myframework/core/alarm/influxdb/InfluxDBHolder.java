package com.myframework.core.alarm.influxdb;

import org.influxdb.InfluxDB;

import java.util.concurrent.TimeUnit;

/**
 * influxdb factory
 * Created by zhangjun
 */
public class InfluxDBHolder {

    //database for default
    private final InfluxDB influxDB;


    public InfluxDBHolder(String dbUrl, String user, String password) {

        if (dbUrl != null && !"".equals(dbUrl) && !"disable".equals(dbUrl)) {
            //monitor, default
            influxDB = org.influxdb.InfluxDBFactory.connect(dbUrl, user, password);
            //>1500个或者50毫秒就开始写入到db中 异步写
            influxDB.enableBatch(1500, 50, TimeUnit.MILLISECONDS);
        } else {
            influxDB = null;
        }
    }


    public InfluxDB getInfluxDB() {
        return influxDB;
    }


}
