package com.myframework.esb;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.myframework.esb.kafka.KafkaMessageCenter;
import com.myframework.esb.system.SystemMessageCenter;
import com.myframework.esb.vo.SwapVO;
import com.myframework.core.filter.RequestFilter;
import com.myframework.util.SpringContextUtil;
import com.myframework.util.StringUtil;

public class MessageCenterHandler
{
	private static Logger logger = LoggerFactory.getLogger(MessageCenterHandler.class);
	private static IMessageCenter messageWorkHandler = null;
	private SystemMessageCenter systemMessageCenter;
	private KafkaMessageCenter kafkaMessageCenter;

	public static final String TOPIC_EVENT = "topic-event";
	public static final String TOPIC_SYSTEMLOG = "topic-systemlog";
	public static final String TOPIC_BUTTONCLICK = "topic-buttonclick";
	public static final String TOPIC_TODO = "topic-todo";
	public static final String TOPIC_BLOG = "topic-blog";
	public static final String TOPIC_XLOC = "topic-xloc";
	public static final String TOPIC_SWAP = "topic-swap";

	public static void init()
	{
		logger.info("prepare for loading context ....");
		if (SpringContextUtil.getContext() == null)
		{
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>初始化系统消息队列<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			while (true)
			{
				try
				{
					Thread.sleep(1000);
					logger.info("Try again init context ....");
					if (SpringContextUtil.getContext() != null)
					{
						logger.info("Try again context init success");
						break;
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		logger.info("context loading completed.");
		MessageCenterHandler messageCenterHandler = (MessageCenterHandler) SpringContextUtil.getBean("messageCenterHandler");
		messageCenterHandler.initWorkHandler().initialize();
	}

	private IMessageCenter initWorkHandler()
	{
		if (messageWorkHandler == null)
		{
			if (isChannelClosed())
			{
				logger.info("消息通道打开方式：系统内置ESB通道");
				messageWorkHandler = this.getSystemMessageCenter();
			}
			else
			{
				logger.info("消息通道打开方式：Kafka消息ESB通道");
				messageWorkHandler = this.getKafkaMessageCenter();
			}
		}
		return messageWorkHandler;
	}

	public static void destroy()
	{
		if (messageWorkHandler != null)
		{
			messageWorkHandler.destroy();
		}
	}

	/**
	 * 判断消息通道是否主动关闭
	 * 
	 * @return true/false
	 */
	public static boolean isChannelClosed()
	{
		return KafkaMessageCenter.isChannelClosed();
	}

	/**
	 * 消费者默认使用精确消费一次方式
	 * 
	 * @param config
	 *            消费者配置
	 * @param topics
	 *            主题
	 * @param listener
	 *            监听接口
	 * @throws MessageCenterException
	 */
	public static void startComsumer(Properties config, List<String> topics, IMessageListener listener)
		throws MessageCenterException
	{
		KafkaMessageCenter.startConsumerExactlyOnce(config, topics, listener);
	}

	/**
	 * 消费者默认使用最少消费一次方式，可能导致数据重复
	 * 
	 * @param config
	 *            消费者配置
	 * @param topics
	 *            主题
	 * @param listener
	 *            监听接口
	 * @throws MessageCenterException
	 */
	public static void startComsumerAtLeastOnce(Properties config, List<String> topics, IMessageListener listener)
		throws MessageCenterException
	{
		KafkaMessageCenter.startComsumerAtLeastOnce(config, topics, listener);
	}

	/**
	 * 消费者默认使用最多消费一次方式，可能导致数据丢失
	 * 
	 * @param config
	 *            消费者配置
	 * @param topics
	 *            主题
	 * @param listener
	 *            监听接口
	 * @throws MessageCenterException
	 */
	public static void startComsumerAtMostOnce(Properties config, List<String> topics, IMessageListener listener)
		throws MessageCenterException
	{
		KafkaMessageCenter.startConsumerAtMostOnce(config, topics, listener);
	}

	/**
	 * 消费者默认使用精确消费一次方式
	 * 
	 * @param config
	 *            消费者配置
	 * @param topics
	 *            主题
	 * @param listener
	 *            监听接口
	 * @throws MessageCenterException
	 */
	public static void startComsumerExactlyOnce(Properties config, List<String> topics, IMessageListener listener)
		throws MessageCenterException
	{
		KafkaMessageCenter.startConsumerExactlyOnce(config, topics, listener);
	}

	public static IMessageCenter getWorkHandler()
	{
		if (messageWorkHandler == null)
		{
			if (isChannelClosed())
			{
				messageWorkHandler = (IMessageCenter) SpringContextUtil.getBean("systemMessageCenter");
			}
			else
			{
				messageWorkHandler = (IMessageCenter) SpringContextUtil.getBean("kafkaMessageCenter");
			}
			if (messageWorkHandler == null)
			{
				logger.warn("加载消息中间件Bean实例出错!");
				/*
				 * if (isChannelClosed()) { messageWorkHandler = new
				 * SystemMessageCenter(); } else { messageWorkHandler = new
				 * KafkaMessageCenter(); }
				 */
				messageWorkHandler = new SystemMessageCenter();
			}
		}
		return messageWorkHandler;
	}

	public static void sendWorkSwap(SwapVO swap) throws MessageCenterException
	{
		if (swap == null)
		{
			return;
		}
		if (isChannelClosed())
		{
			return;
		}
		getWorkHandler().send(TOPIC_SWAP, String.valueOf(swap.getTenantId()), swap.toString());
	}

	public static void sendSystemLog(String key, String str) throws MessageCenterException
	{
		getWorkHandler().send(TOPIC_SYSTEMLOG, key, str);
	}

	/**
	 * 按钮点击事件
	 * 
	 * @param params
	 */
	public static void sendButtonClick(Map<String, String> params) throws MessageCenterException
	{
		String menuId = StringUtil.valueOf(params.get("menuId"));
		JSONObject message = (JSONObject) JSONObject.toJSON(params);
		getWorkHandler().send(TOPIC_BUTTONCLICK, menuId, message.toString());
	}

	/**
	 * 按消息主题推送消息
	 * 
	 * @param topic
	 *            主题
	 * @param key
	 *            消息分组
	 * @param str
	 *            消息正文
	 */
	public static void sendMessageByTopic(String topic, String key, String str) throws MessageCenterException
	{
		getWorkHandler().send(topic, key, str);
	}

	/**
	 * 使用外部ESB总线暂时不启用
	 * 
	 * @param event
	 *            事件类型
	 * @param in
	 *            输入参数
	 */
	private static void sendSystemEvent(String event, Map<String, Object> in) throws MessageCenterException
	{
		Long tenantId = (Long) RequestFilter.getRequest().getSession().getAttribute("tenantId");
		sendSystemEvent(event, in, tenantId);
	}

	/**
	 * 使用外部ESB总线暂时不启用
	 * 
	 * @param event
	 *            事件类型
	 * @param in
	 *            输入参数
	 * @param tenantId
	 *            企业ID
	 */
	private static void sendSystemEvent(String event, Map<String, Object> in, Long tenantId)
		throws MessageCenterException
	{
		if (tenantId > 0)
		{
			JSONObject message = (JSONObject) JSONObject.toJSON(in);
			message.put("_current_tenant_id_", tenantId);
			getWorkHandler().send(TOPIC_EVENT, event, message.toString());
		}
	}

	public SystemMessageCenter getSystemMessageCenter() {
		return systemMessageCenter;
	}

	public void setSystemMessageCenter(SystemMessageCenter systemMessageCenter) {
		this.systemMessageCenter = systemMessageCenter;
	}

	public KafkaMessageCenter getKafkaMessageCenter() {
		return kafkaMessageCenter;
	}

	public void setKafkaMessageCenter(KafkaMessageCenter kafkaMessageCenter) {
		this.kafkaMessageCenter = kafkaMessageCenter;
	}
}
