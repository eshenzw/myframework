package com.myframework.core.alarm;

/**
 * 事件类型
 * 
 * created by zw
 *
 */
public enum EventTypeEnum {

	// 服务异常
	EVENT_TYPE_SERVER_EX("server_monitor", "server_exception"),

	// 数据库访问
	EVENT_TYPE_DB_ACCESS("server_monitor", "db_access"),

	// 服务调用
	EVENT_TYPE_SERVICE_ACCESS("server_monitor", "service_access"),

	// 服务间调用
	EVENT_TYPE_SERVICE_CALL("server_monitor", "service_call"),

	// session 异常
	EVENT_TYPE_SESSION_EX("server_monitor", "session_exception"),
	
	//前置机 服务访问
	EVENT_TYPE_AGENT_SERVICE_ACCESS("server_monitor", "agent_service_access"),
	
	//rabbit mq 发送事件
	EVENT_TYPE_RABBIT_SEND("server_monitor", "rabbit_send"),



	;

	/**
	 * 苦命
	 */
	private String dbName;

	/**
	 * 表名
	 */
	private String measurements;
	
	

	public String getDbName() {
		return dbName;
	}



	public void setDbName(String dbName) {
		this.dbName = dbName;
	}



	public String getMeasurements() {
		return measurements;
	}



	public void setMeasurements(String measurements) {
		this.measurements = measurements;
	}



	/**
	 * 
	 * @param dbName
	 * @param tableName
	 */
	private EventTypeEnum(String dbName, String measurements) {

		this.dbName = dbName;

		this.measurements = measurements;
	}

}