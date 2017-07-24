package com.myframework.core.alarm.influxdb;


import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.myframework.core.alarm.EventReporter;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 写入事件到InfluxData中
 * <p/>
 * <p/>
 * <p/>
 * refer to :  https://github.com/influxdata/influxdb-java
 * Created by zhangjun
 */
public class InfluxDataReporter implements EventReporter {

    private Logger log = LoggerFactory.getLogger(getClass());


    private String retentionPolicy;
    //database
    private InfluxDB influxDB;

    private Random random = new Random();



    public InfluxDataReporter(InfluxDBHolder influxDBHolder,String retentionPolicy){
        this.influxDB = influxDBHolder.getInfluxDB();
        this.retentionPolicy = retentionPolicy;

    }


    @Override
    public void report(String database, String measurement, Map<String, String> tags, Map<String, Object> fields) {

       InfluxDB db = this.getDatabase(database);

        Point.Builder pointBuilder = Point.measurement(measurement).time(System.currentTimeMillis() * 1000000 + random.nextInt(999999), TimeUnit.NANOSECONDS);


        //add tags
        if (tags != null) {
            for (Map.Entry<String, String> tagEntry : tags.entrySet()) {
                pointBuilder.tag(tagEntry.getKey(), tagEntry.getValue());
            }
        }

        // add fields
        if (fields != null) {
            for (Map.Entry<String, Object> otherEntry : fields.entrySet()) {
                //ignore tag
                if (tags != null && tags.keySet() != null && tags.keySet().contains(otherEntry.getKey())) {
                    continue;
                }

                //ignore null
                if (otherEntry.getKey() == null || otherEntry.getValue() == null) {
                    continue;
                }

                pointBuilder.field(otherEntry.getKey(),  otherEntry.getValue());
            }
        }

        //never throw exception
        try {
            db.write(database, retentionPolicy, pointBuilder.build());
            log.debug("write {} with tags{} to {} success.", measurement, tags, database);
        } catch (Exception e) {
            log.error("write to influxdb error", e);
        }
    }


    private InfluxDB getDatabase(String databaseName){

        InfluxDB db ;

        switch (databaseName){
            case "server_monitor": {
                db = this.influxDB;
                break;
            }

            default:{
                db = this.influxDB;
            }
        }

        return db;

    }


}
