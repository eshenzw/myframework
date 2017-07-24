package com.myframework.core.zookeeper.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.myframework.core.zookeeper.listener.custom.ZKConnectionListener;
import com.myframework.core.zookeeper.listener.custom.ZkNodeListener;
import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 管理自定义的节点监听器
 * 
 * @author zhangjun
 *
 */
public class CustomerZKListenerMgr implements ApplicationContextAware {

	private Map<String, List<ZkNodeListener>> zkNodeListenerMap = Maps.newConcurrentMap();

	private List<ZKConnectionListener> zkConnectionListenerList = new ArrayList<ZKConnectionListener>();

	private Map<String, DefaultNodeCacheListener> watchMap = Maps.newConcurrentMap();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		Map<String, ZkNodeListener> zkNodeListener = applicationContext.getBeansOfType(ZkNodeListener.class);

		addZkNodeListener(zkNodeListener);

		Map<String, ZKConnectionListener> zkConnectionListeners = applicationContext
				.getBeansOfType(ZKConnectionListener.class);

		addZkConnectionListener(zkConnectionListeners);

	}

	/**
	 * 注册 节点监听器
	 * 
	 * @param map
	 */
	private void addZkNodeListener(Map<String, ZkNodeListener> consumerBeans) {

		if (consumerBeans == null || consumerBeans.isEmpty()) {
			return;
		}

		for (Entry<String, ZkNodeListener> entry : consumerBeans.entrySet()) {

			ZkNodeListener listener = entry.getValue();

			List<ZkNodeListener> list = zkNodeListenerMap.get(listener.getMonitorPath());

			if (list == null) {

				zkNodeListenerMap.put(listener.getMonitorPath(), Lists.newArrayList(listener));

			} else {
				list.add(listener);
			}

		}

	}

	/**
	 * 注册连接状态监听器
	 * 
	 * @param consumerBeans
	 */
	private void addZkConnectionListener(Map<String, ZKConnectionListener> consumerBeans) {

		if (consumerBeans == null || consumerBeans.isEmpty()) {
			return;
		}

		for (Entry<String, ZKConnectionListener> entry : consumerBeans.entrySet()) {

			zkConnectionListenerList.add(entry.getValue());

		}
	}

	/**
	 * 获取监听path的 listener
	 * 
	 * @param path
	 * @return
	 */
	public List<ZkNodeListener> getNodePathMonitorListeners(String path) {

		if (zkNodeListenerMap == null || zkNodeListenerMap.isEmpty()) {
			return null;
		}

		return zkNodeListenerMap.get(path);

	}

	/**
	 * 获取当前所有在监听的节点path
	 * 
	 * @return
	 */
	public List<String> getAllNodeMonitorPaths() {

		if (zkNodeListenerMap == null || zkNodeListenerMap.isEmpty()) {
			return null;
		}

		List<String> list = new ArrayList<String>();

		list.addAll(zkNodeListenerMap.keySet());

		return list;
	}

	/**
	 * 绑定监听器到 zk
	 * 
	 */
	public void bindAllListener(CuratorFramework curatorFramework) {

		// 注册连接监听器
		if (CollectionUtils.isNotEmpty(zkConnectionListenerList)) {

			curatorFramework.getConnectionStateListenable().addListener(new DefaultConnectionStateListener());
		}

		// 注册节点监听器
		if (zkNodeListenerMap != null && !zkNodeListenerMap.isEmpty()) {

			for (String path : zkNodeListenerMap.keySet()) {

				//构造函数起到了实际注册的作用
				watchMap.put(path, new DefaultNodeCacheListener(path, curatorFramework, this));
			}

		}

	}

}