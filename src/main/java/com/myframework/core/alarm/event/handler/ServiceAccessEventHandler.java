package com.myframework.core.alarm.event.handler;


import java.util.Map;

import com.myframework.core.alarm.event.ServiceAccessEvent;



/**
 *   service access event：所有的服务访问事件
 *
 * Created by zhangjun.
 */
public class ServiceAccessEventHandler extends AbstractEventHandler<ServiceAccessEvent> {

    /**
     * 设置参数
     */
    @Override
    protected  void addArgs(ServiceAccessEvent event, Map<String, Object> args){

        if(event.getStack() != null){
            args.put("stack", event.getStack());
        }

        args.put("cost", event.getCost());
        
        //用户id
        if(event.getUserId()>0) {
        	args.put("user_id",  String.valueOf(event.getUserId()));
        }

        //企业id
        if(event.getTenantId()>0) {
        	args.put("tenant_id",  String.valueOf(event.getTenantId()));
        }

    }


    /**
     * 设置tags
     */
    @Override
    protected  void addTags(ServiceAccessEvent event, Map<String, String> tags){
        tags.put("status_code", String.valueOf(event.getResponse_code()));
        tags.put("src_service",  event.getSrcService());

    }

    @Override
    protected String getDatabase() {
        return SERVER_EVENT;
    }

    @Override
    String getCatalog(ServiceAccessEvent event) {
        return "service_access";
    }
}
