package com.myframework.core.alarm;

import java.util.Map;

/**
 * 告警上报事件
 * 
 * created by zw
 */
public interface EventReporter {

	
	  /**
	   * 上报一个事件
	   * @param database database
	   * @param measurement 表名字
	   * @param tags tags会建立索引。默认会用event中的event建立索引
	   * @param fields
	   */
      void report(String database, String measurement, Map<String, String> tags, Map<String, Object> fields) ;


    }
