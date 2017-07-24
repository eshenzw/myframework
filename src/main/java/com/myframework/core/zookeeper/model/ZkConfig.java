package com.myframework.core.zookeeper.model;

/**
 * created by zw
 *
 */
public class ZkConfig {
     
    /**
     * 集群zk名称
     */
    private String name;
    
    /**
     * zk服务url地址,集群之间url用逗号分隔
     */
    private String url;
    /**
     * session超时时间
     */
    private Integer sessionTimeoutMs;
    
    /**
     * 连接超时时间
     */
    private Integer connectionTimeoutMs;
    
    /**
     * 重试次数
     */
    private Integer retryTimes;
    
    /**
     * initial amount of time to wait between retries
     */
    private Integer retrySleepTimeMs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(Integer sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public Integer getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Integer getRetrySleepTimeMs() {
        return retrySleepTimeMs;
    }

    public void setRetrySleepTimeMs(Integer retrySleepTimeMs) {
        this.retrySleepTimeMs = retrySleepTimeMs;
    }
}
