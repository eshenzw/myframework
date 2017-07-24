package com.myframework.core.zookeeper.factory;

import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.myframework.core.zookeeper.exception.ZKConnectException;
import com.myframework.core.zookeeper.listener.CustomerZKListenerMgr;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSON;

/**
 * zookeeper注册工厂类
 * 
 * 
 * @author zhangjun
 *
 */
public class ZookeeperClientFactory implements FactoryBean<CuratorFramework>, InitializingBean, DisposableBean {
	private final static Logger logger = LoggerFactory.getLogger(ZookeeperClientFactory.class);

	private CustomerZKListenerMgr customerZKListenerMgr;

	private CuratorFramework curator;
	private String connectString;
	private RetryPolicy retryPolicy;
	private Integer sessionTimeout;
	private String namespace;

	/**
	 * 是否开启zk注册
	 */
	private String enableZkReg;

	public void afterPropertiesSet() throws Exception {

		if (StringUtils.isEmpty(connectString) || "disable".equals(connectString)) {
			return;
		}

		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();

		builder.connectString(connectString);

		if (retryPolicy == null) {
			retryPolicy = new ExponentialBackoffRetry(1000, 3);
		}
		builder.retryPolicy(retryPolicy);

		if (sessionTimeout != null) {
			builder.sessionTimeoutMs(sessionTimeout);
		}

		if (namespace != null) {
			builder.namespace(namespace);
		}

		curator = builder.build();

		final CountDownLatch downLactch = new CountDownLatch(1);

		curator.getConnectionStateListenable().addListener(new ConnectionStateListener() {

			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				
				logger.info("ZookeeperClientFactory  Zookeeper connect stat change ! newState ="+JSON.toJSONString(newState));
				if (newState == ConnectionState.CONNECTED || newState == ConnectionState.RECONNECTED) {
					downLactch.countDown();
					logger.info("with zk server connection is ok!!!!");
				}
			}
		});

		curator.start();

		try {
			downLactch.await(3000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.warn("connection the url:{} failed at connectionTimeoutMs:{}", connectString, 3000);
			String message = MessageFormat.format("connection the url:{0} failed at connectionTimeoutMs:{1}",
					connectString, 3000);
			throw new ZKConnectException(message, e);
		}

		// 注册节点监听和连接状态监听
		customerZKListenerMgr.bindAllListener(curator);

		logger.info("start zookeeper client success. connected to {}", connectString);
	}

	public void destroy() throws Exception {
		if(curator!=null)
		      curator.close();
	}

	public CuratorFramework getObject() throws Exception {
		return curator;
	}

	public Class<?> getObjectType() {
		return CuratorFramework.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public String getConnectString() {
		return connectString;
	}

	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}

	public Integer getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(Integer sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public RetryPolicy getRetryPolicy() {
		return retryPolicy;
	}

	public void setRetryPolicy(RetryPolicy retryPolicy) {
		this.retryPolicy = retryPolicy;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getEnableZkReg() {
		return enableZkReg;
	}

	public void setEnableZkReg(String enableZkReg) {
		this.enableZkReg = enableZkReg;
	}

	public CustomerZKListenerMgr getCustomerZKListenerMgr() {
		return customerZKListenerMgr;
	}

	public void setCustomerZKListenerMgr(CustomerZKListenerMgr customerZKListenerMgr) {
		this.customerZKListenerMgr = customerZKListenerMgr;
	}



}