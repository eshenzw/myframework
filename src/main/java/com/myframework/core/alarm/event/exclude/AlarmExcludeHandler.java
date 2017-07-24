package com.myframework.core.alarm.event.exclude;

import java.util.Map;


import com.myframework.core.alarm.EventTypeEnum;

/**
 * 告警排除项目：
 * 
 * 那些action不需要告警，比如导入 导出 之类
 * 
 * created by zw
 *
 */
public class AlarmExcludeHandler {
	

	private Map<String,Object> serviceAccessExcludeMap ;
		

	public Map<String, Object> getServiceAccessExcludeMap() {
		return serviceAccessExcludeMap;
	}


	public void setServiceAccessExcludeMap(Map<String, Object> serviceAccessExcludeMap) {
		this.serviceAccessExcludeMap = serviceAccessExcludeMap;
	}


	/**
	 * 根据事件类型，获取对应的 排除 action的信息
	 * @param eventType
	 * @return
	 */
	public Map<String,Object> getExcludeMapByEventType(EventTypeEnum eventType) {
		
		
		switch(eventType) {
		
		case EVENT_TYPE_SERVICE_ACCESS:
		     return serviceAccessExcludeMap;
		     
		     
		     default:
		    	 return null;
		}

	}

}