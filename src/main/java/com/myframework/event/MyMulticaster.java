package com.myframework.event;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import com.myframework.db.DynamicDataSource;
import com.myframework.filter.RequestFilter;
import com.myframework.util.SpringContextUtil;

/***
 * 事件广播分发类
 * 
 * @author zw
 * 
 */
public class MyMulticaster
{
	/** 处理信息的监听对象缓存 */
	protected final static Map<String, List<IMyEventListener>> listenerCache = new ConcurrentHashMap<String, List<IMyEventListener>>();
	/** 所有实现了IMyEventListener接口的类,通过调用spring的getBeansOfType方法获取 **/
	protected final static Set<IMyEventListener> allListenerBeans = new LinkedHashSet<IMyEventListener>();
	/** 日志 */
	protected final static Logger logger = LoggerFactory.getLogger(MyMulticaster.class);
	/** 执行通知的线程池 */
	protected static TaskExecutor eventMulticastThreadPool;

	public void init()
	{
		Map<String, IMyEventListener> beansMap = SpringContextUtil.getContext().getBeansOfType(
				IMyEventListener.class, false, false);
		for (String beanName : beansMap.keySet())
		{
			allListenerBeans.add(beansMap.get(beanName));
			logger.info("检测到并加载了MyEvent监听类:{}", beanName);
		}
	}

	/***
	 * 广播通知
	 * 
	 * @param event
	 */
	public static void multicastEvent(final MyEvent event)
	{
		Collection<IMyEventListener> listeners = getListeners(event);
		if (event.getConnId() == null || event.getConnId() < 1)
		{
			Long connId = (Long) RequestFilter.getSession().getAttribute(DynamicDataSource.SESSION_CONN_KEY);
			event.setConnId(connId);
		}
		final Long connId = event.getConnId();
		for (final IMyEventListener listener : listeners)
		{
			eventMulticastThreadPool.execute(new Runnable()
			{
				public void run()
				{
					DynamicDataSource.setCurConn(connId);
					listener.onMyEvent(event);
				}
			});
		}
		logger.debug("事件{}被通知了{}个监听类", event.getName(), listeners.size());
	}

	/**
	 * 同步广播通知
	 * 
	 * @param event
	 * @return 执行结果
	 */
	public static boolean syncMulticastEvent(final MyEvent event)
	{
		Collection<IMyEventListener> listeners = getListeners(event);
		for (final IMyEventListener listener : listeners)
		{
			listener.onMyEvent(event);
		}
		return true;
	}

	protected static Collection<IMyEventListener> getListeners(MyEvent event)
	{
		List<IMyEventListener> listeners = listenerCache.get(event.getName());
		if (listeners == null)
		{
			listeners = new ArrayList<IMyEventListener>();
			for (IMyEventListener lis : allListenerBeans)
			{
				if (lis.supportsEvent(event.getName()))
				{
					listeners.add(lis);
				}
			}
			// 缓存此类消息的监听类
			listenerCache.put(event.getName(), listeners);
		}
		return listeners;
	}

	public void setEventMulticastThreadPool(TaskExecutor eventMulticastThreadPool)
	{
		this.eventMulticastThreadPool = eventMulticastThreadPool;
	}
}
