package com.myframework.core.alarm.event;


import java.util.Map;

import com.myframework.core.alarm.EventTypeEnum;

/**
 *  rabbitmq 发送事件
 *
 * .
 */
public class RabbitmqSentEvent extends CommonEvent {

    /**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = -7120188904547178276L;
	private final Object message;
    private final Map<String, Object> pros;
    private final String routingKey;
    //发送的事件类型 topic，fanout，direct，topic_delay
    private final String type;
    

    /**
     *  rabbit 发送调用事件
     */
    public RabbitmqSentEvent(String exchange, String  routingKey, Object message,  Map<String, Object> pros,String type) {
        super(exchange);
        this.routingKey = routingKey;
        this.message = message;
        this.pros = pros;
        this.type = type;
    }

    public Object getMessage() {
        return message;
    }

    public Map<String, Object> getPros() {
      return pros;
    }

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.EVENT_TYPE_RABBIT_SEND;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public String getType() {
		return type;
	}



}
