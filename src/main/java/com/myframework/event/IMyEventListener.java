package com.myframework.event;

/***
 * 处理事件类需要实现的接口 .其他模块发送事件时候，事件消息会被广播给实现此接口的类对象
 * 
 * @author zw
 * 
 */
public interface IMyEventListener
{
	public boolean supportsEvent(String eventName);

	/***
	 * 此方法在线程中异步调用，所以不可以直接使用session中的任何信息，需要的任何数据可以从event的data中传递过来
	 * 
	 * @param event
	 */
	public void onMyEvent(MyEvent event);
}
