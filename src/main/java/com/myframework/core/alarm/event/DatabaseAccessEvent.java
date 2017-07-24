package com.myframework.core.alarm.event;

import com.myframework.core.alarm.EventTypeEnum;

/**
 *  数据库操作的事件
 * Created by zhangjun
 */
public class DatabaseAccessEvent extends  CommonEvent {


    /**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = -2863457811659066198L;
	private final Throwable throwable;
    private final long cost;
    private final String datasource;
    private final String statement;

    //服务的名称
    private String serviceName= "UNKNOWN";


    //最原始的服务名称，来自gateway
    private String srcServiceName = "UNKNOWN";


    /**
     * Create a new ApplicationEvent.
     *
     * @param name : dao name : 例如： com.yoho.IProductDAO
     * @param  statement: 接口名称，例如 getAll
     * @param  throwable: 数据库操作的异常， 可能为null
     * @param cost: 数据库操作的延迟
     * @param datasource: 数据库名称
     *
     */
    public DatabaseAccessEvent(String name, String statement, Throwable throwable, long cost, String datasource) {
        super(name);
        this.statement = statement;
        this.throwable = throwable;
        this.cost = cost;
        this.datasource = datasource;
    }


    /**
     * 兼容之前的方法。
     * @return args
     */
    @Deprecated
    public String getArgs(){
        return this.datasource;
    }

    public Throwable getThrowable() {
        return throwable;
    }
    public long getCost() {
        return cost;
    }

    public String getDatasource() {
        return datasource;
    }

    public String getStatement() {
        return statement;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public String getSrcServiceName() {
        return srcServiceName;
    }

    public void setSrcServiceName(String srcServiceName) {
        this.srcServiceName = srcServiceName;
    }


	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.EVENT_TYPE_DB_ACCESS;
	}
}
