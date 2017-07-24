package com.myframework.core.zookeeper.election;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * recipes包里面提供了Leader选举实现，Spark中的master选举使用的就是reciples包里面的LeaderLatch，
 * 使用他们可以极大的简化代码，使你将注意力更多的放在核心业务逻辑上
 * 
 * Leader选举的实现在org.apache.curator.framework.recipes.leader包中，这个包提供了两组Leader选举：
 * 1.LeaderLatch,LeaderLatchListener，leader latch里实现了ConnectionStateListener ，监听状态变化
 * 2.LeaderSelector,LeaderSelectorListener,LeaderSelectorListenerAdapter。
 * 
 * 这里实现的是第一个方法
 * <p/>
 * 
 */
public class ZkLeaderLatcher implements ApplicationContextAware {

	private final static String PATH = "/myframework/leader_latch/";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<String, List<ZkLeaderLatcherListener>> leaderLatcherListenerMap = Maps.newConcurrentMap();

	
	private final Map<String,LeaderLatch> latchs = new HashMap<String,LeaderLatch>();


	// add
	private final CuratorFramework client;

	/**
	 * final
	 *
	 * @param client
	 */
	public ZkLeaderLatcher(CuratorFramework client) {
		this.client = client;
	}

	/**
	 * close, called by spring
	 */
	public void close() throws IOException {
		
		if(latchs==null || latchs.isEmpty()) {
			return;
		}
		
		
		for (LeaderLatch leaderSelector : this.latchs.values()) {
			leaderSelector.close();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, ZkLeaderLatcherListener> listeners = applicationContext
				.getBeansOfType(ZkLeaderLatcherListener.class);
		//有可能没注册zk
		if (MapUtils.isEmpty(listeners) || client == null) {
			return;
		}

		gererateListenerByLatchMap(listeners);

		for (Entry<String, List<ZkLeaderLatcherListener>> entry : leaderLatcherListenerMap.entrySet()) {

			final String latchType = entry.getKey();
			// create path
			String path = PATH + latchType;
			try {
				if (client.checkExists().forPath(path) == null) {
					client.create().creatingParentContainersIfNeeded().forPath(path);
				}
			} catch (Exception e) {
				logger.error("can not create path:{}", path);
				continue;
			}

			// add latch
			LeaderLatch leaderLatch = new LeaderLatch(client, path, latchType);
			
			leaderLatch.addListener(new LeaderLatchListener() {

				@Override
				public void isLeader() {
					
				    // could have lost leadership by now.  
				    //现在leadership可能已经被剥夺了。。详情参见Curator的实现。
					LeaderLatch latch = latchs.get(latchType);
					if(latch==null || !latch.hasLeadership()) {
						return;
					}
					
					triggerSetupLeader(true, latchType);
				}

				@Override
				public void notLeader() {
					
				    // could have  leadership by now.  
				    //现在leadership可能已经被赋予leader了。。详情参见Curator的实现。
					LeaderLatch latch = latchs.get(latchType);

					if(latch==null || latch.hasLeadership()) {
						return;
					}
					
					triggerSetupLeader(false, latchType);
				}
			});

			try {
				leaderLatch.start();
				
				latchs.put(latchType, leaderLatch);

			} catch (Exception e) {
				logger.error("start latch error", e);
			}
		}

	}


	/**
	 * 将spring bean 注册的listener做处理
	 * @param map
	 */
	private void gererateListenerByLatchMap(Map<String, ZkLeaderLatcherListener> map) {

		for (ZkLeaderLatcherListener listener : map.values()) {

			List<ZkLeaderLatcherListener> list = leaderLatcherListenerMap.get(listener.getLatchType());

			if (list == null) {
				leaderLatcherListenerMap.put(listener.getLatchType(), Lists.newArrayList(listener));
			} else {
				list.add(listener);
			}

		}

	}

	/**
	 *  触发listen里的事件
	 * @param isLeader
	 * @param latchType
	 */
	private void triggerSetupLeader(boolean isLeader, String latchType) {

		List<ZkLeaderLatcherListener> list = leaderLatcherListenerMap.get(latchType);

		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		for (ZkLeaderLatcherListener listener : list) {
			listener.setupLeader(isLeader);

		}

	}

}
