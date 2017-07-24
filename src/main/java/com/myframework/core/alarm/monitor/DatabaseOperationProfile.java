package com.myframework.core.alarm.monitor;

import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 打印数据库DAO执行的统计信息,包括DAO执行的次数,时间分布。
 * 
 * 1分钟dump一次日志
 * 
 * created by zw
 */
public class DatabaseOperationProfile {

    private static final Map<String,int[]> dbOperations = new LinkedHashMap<String,int[]>();

    //打印运行日志
    private static final Logger runLogger = LoggerFactory.getLogger(DatabaseOperationProfile.class);


    static {
    	//一分钟 dump一次
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while(true) {
                    try {
                        //every minute
                        Thread.sleep(60 * 1000);
                        DatabaseOperationProfile.dump();
                    } catch (Exception e) {
                        //do nothing
                    }
                }
            }
        });
        thread.start();
    }

    /**
     *  db操作的拦截器调用,记录每个dao执行的时间，分成3个区间，
     *  0-100
     *  100-500
     *  500以上
     * 
     * @param dao
     * @param duration
     */
    public static synchronized void put(String dao, int duration){

        //为每个dao操作记录时间分布
        int[] timesArr = dbOperations.get(dao) ;
        if( timesArr == null ){
            timesArr = new int[3];
            dbOperations.put(dao,timesArr);
        }

        if( duration >= 0 && duration < 100){
            timesArr[0]++;    // [0,100)
        }else if(duration >=100 && duration < 500 ){
            timesArr[1]++;    // [100,500)
        }else {
            timesArr[2]++;    // [500,)
        }

    }

    /**
     * dump所有dao操作的统计数据
     */
    private static synchronized void dump(){

        DBStaticInfo dbInfo = new DBStaticInfo();
        for(Map.Entry<String,int[]> entry: dbOperations.entrySet()){

            int timesArr[] = entry.getValue() ;
            if( timesArr == null || entry.getKey() == null){
                continue;
            }

            dbInfo.dao = entry.getKey() ;
            dbInfo.section0 = timesArr[0] ;
            dbInfo.section1 = timesArr[1] ;
            dbInfo.section2 = timesArr[2] ;

            runLogger.info(JSONObject.toJSONString(dbInfo));
        }

    }


}

class DBStaticInfo{
     String dao;

    public int getSection2() {
        return section2;
    }

    public void setSection2(int section2) {
        this.section2 = section2;
    }

    public String getDao() {
        return dao;
    }

    public void setDao(String dao) {
        this.dao = dao;
    }

    public int getSection0() {
        return section0;
    }

    public void setSection0(int section0) {
        this.section0 = section0;
    }

    public int getSection1() {
        return section1;
    }

    public void setSection1(int section1) {
        this.section1 = section1;
    }

    int section0;
     int section1;
     int section2;
}
