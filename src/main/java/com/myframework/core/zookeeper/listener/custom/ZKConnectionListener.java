package com.myframework.core.zookeeper.listener.custom;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;



/**
 * 连接重建的监听器，在连接重建时，需要对节点的监听重新注册<br>
 * @author zhangjun
 *
 */
public interface ZKConnectionListener {
    /**
     * 
     * 功能描述: 连接重建的通知
     * 〈功能详细描述〉
     *
     * @param client
     * @param newState
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    void stateChanged(CuratorFramework client, ConnectionState newState);
}
