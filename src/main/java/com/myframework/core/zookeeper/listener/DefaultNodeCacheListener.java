package com.myframework.core.zookeeper.listener;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.myframework.core.zookeeper.listener.custom.ZkNodeListener;
import com.myframework.core.zookeeper.model.ZkNodeData;
import com.myframework.core.zookeeper.util.ZkPathUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 节点监听类
 *
 * created by zw
 */
public class DefaultNodeCacheListener implements NodeCacheListener, PathChildrenCacheListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // private Map<String, Set<ZkNodeListener>> zkNodeListenerMap =
    // Maps.newConcurrentMap();

    private CuratorFramework curator;

    private NodeCache nodeCache;

    private Charset charset = Charset.forName("UTF-8");

    private CustomerZKListenerMgr customerZKListenerMgr;

    @SuppressWarnings("resource")
    public DefaultNodeCacheListener(String path, CuratorFramework curator,
                                    CustomerZKListenerMgr customerZKListenerMgr) {
        this.curator = curator;
        this.customerZKListenerMgr = customerZKListenerMgr;
        nodeCache = new NodeCache(curator, path);
        PathChildrenCache pathNode = new PathChildrenCache(curator, path, true);
        try {
            nodeCache.start();
            pathNode.start(StartMode.BUILD_INITIAL_CACHE);
        } catch (Exception e) {
            log.error("node start failed", e);
        }
        nodeCache.getListenable().addListener(this);
        pathNode.getListenable().addListener(this);
    }

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        if (null == event.getData() || null == event.getData().getData()) {
            log.warn("childEvent fired failed,the data is null!!!");
            return;
        }

        byte[] data = event.getData().getData();
        String path = event.getData().getPath();
        ZkNodeData nodeData = new ZkNodeData(path, new String(data, charset), null);
        List<ZkNodeListener> zkNodeListeners = customerZKListenerMgr.getNodePathMonitorListeners(path);
        // 获取对父节点的监听
        List<ZkNodeListener> parentZKNodeListeners = customerZKListenerMgr
                .getNodePathMonitorListeners(ZkPathUtils.getParentPath(path));
        List<ZkNodeListener> all_listeners = new ArrayList<ZkNodeListener>();

        if (CollectionUtils.isNotEmpty(zkNodeListeners)) {
            all_listeners.addAll(zkNodeListeners);
        }
        if (CollectionUtils.isNotEmpty(parentZKNodeListeners)) {
            all_listeners.addAll(parentZKNodeListeners);
        }
        if (CollectionUtils.isEmpty(all_listeners)) {
            log.info("no listeners for this path:{}", path);
            return;
        }

        invokeZkNodeListener(event, nodeData, all_listeners);
    }

    private void invokeZkNodeListener(PathChildrenCacheEvent event, ZkNodeData nodeData,
                                      List<ZkNodeListener> allListeners) {
        if (event.getType().equals(Type.CHILD_ADDED)) {
            for (ZkNodeListener zkNodeListener : allListeners) {
                if (zkNodeListener.accept(nodeData)) {
                    zkNodeListener.childAdded(nodeData);
                }
            }
        } else if (event.getType().equals(Type.CHILD_UPDATED)) {
            for (ZkNodeListener zkNodeListener : allListeners) {
                if (zkNodeListener.accept(nodeData)) {
                    zkNodeListener.childUpdated(nodeData);
                }
            }
        } else if (event.getType().equals(Type.CHILD_REMOVED)) {
            for (ZkNodeListener zkNodeListener : allListeners) {
                if (zkNodeListener.accept(nodeData)) {
                    zkNodeListener.childDeleted(nodeData);
                }
            }
        } else {
            log.error("no type for this:{}", event.getType());
        }
    }

    @Override
    public void nodeChanged() throws Exception {

        if (nodeCache.getCurrentData() == null || nodeCache.getCurrentData().getData() == null) {
            return;
        }

        byte[] data = nodeCache.getCurrentData().getData();
        String path = nodeCache.getCurrentData().getPath();

        List<ZkNodeListener> zkNodeListeners = customerZKListenerMgr.getNodePathMonitorListeners(path);

        ZkNodeData nodeData = new ZkNodeData(path, new String(data, charset), null);
        if (CollectionUtils.isNotEmpty(zkNodeListeners)) {
            for (ZkNodeListener zkNodeListener : zkNodeListeners) {
                if (zkNodeListener.accept(nodeData)) {
                    zkNodeListener.nodeUpdated(nodeData);
                }
            }
        }
    }

    public CuratorFramework getCurator() {
        return curator;
    }

    public CustomerZKListenerMgr getCustomerZKListenerMgr() {
        return customerZKListenerMgr;
    }

    public void setCustomerZKListenerMgr(CustomerZKListenerMgr customerZKListenerMgr) {
        this.customerZKListenerMgr = customerZKListenerMgr;
    }

}
