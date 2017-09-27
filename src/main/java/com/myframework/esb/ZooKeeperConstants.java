package com.myframework.esb;

public final class ZooKeeperConstants {
    // Kafka Offset值
    public static final String KAFKA_OFFSET_FORMAT = "/myframework/appsvr/kafka/%s-%s-%s";
    // app选举
    public static final String JOB_LEADER = "/myframework/app/leader";
    //应用包发布
    public static final String PACKAGE_DEPLOY = "/myframework/app/packagedeploy";
}
