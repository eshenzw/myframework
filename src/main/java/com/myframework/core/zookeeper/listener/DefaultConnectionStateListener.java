package com.myframework.core.zookeeper.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.myframework.core.zookeeper.listener.custom.ZKConnectionListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

/**
 * 监听zookeeper连接状态的变更，心跳断了重连
 * 
 * 屏蔽上层业务对于底层细节的理解，包装对连接监听的处理
 * 
 * created by zw
 *
 */
public class DefaultConnectionStateListener implements ConnectionStateListener, ApplicationContextAware {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<ZKConnectionListener> connectionList = new ArrayList<ZKConnectionListener>();

	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {

		customStateChanged(client, newState);
	}

	/**
	 * 通知自定义的监听器
	 * 
	 * @param client
	 * @param newState
	 */
	private void customStateChanged(CuratorFramework client, ConnectionState newState) {

		if (CollectionUtils.isEmpty(connectionList)) {
			return;
		}

		for (ZKConnectionListener zkConnectionListener : connectionList) {
			zkConnectionListener.stateChanged(client, newState);
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		Map<String, ZKConnectionListener> consumerBeans = applicationContext.getBeansOfType(ZKConnectionListener.class);

		if (consumerBeans == null || consumerBeans.isEmpty()) {
			return;
		}

		for (Entry<String, ZKConnectionListener> entry : consumerBeans.entrySet()) {
			connectionList.add(entry.getValue());
		}

	}

	public List<ZKConnectionListener> getConnectionList() {
		return connectionList;
	}

	public void setConnectionList(List<ZKConnectionListener> connectionList) {
		this.connectionList = connectionList;
	}

}
