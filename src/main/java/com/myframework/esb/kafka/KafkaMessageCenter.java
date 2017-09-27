package com.myframework.esb.kafka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.myframework.esb.MessageQueueConsumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myframework.constant.Constants;
import com.myframework.esb.IMessageCenter;
import com.myframework.esb.IMessageListener;
import com.myframework.esb.MessageCenterException;
import com.myframework.util.PropertiesUtil;

public class KafkaMessageCenter implements IMessageCenter
{
	private static Logger logger = LoggerFactory.getLogger(KafkaMessageCenter.class);
	private static Properties system = PropertiesUtil.loadProperties(Constants.SYSTEM_FILE_PATH);
	public static Producer<String, String> producer = null;

	private static KafkaOffsetManager offsetManager = new KafkaOffsetManager("offset");
	private static String kafkaOpen = system.getProperty("kafka.open");
	private static String kafkaConsumerOpen = system.getProperty("kafka.consumer.open");
	private static List<KafkaConsumer<String, String>> consumers = new ArrayList<KafkaConsumer<String, String>>();
	public static boolean keepRunning = true;

	/**
	 * spring bean initialize
	 */
	public void initialize()
	{
		logger.info("Initializing KafkaMessageCenter");
		ZookeeperManager.init();
		Properties propProduct = new Properties();
		propProduct.put("bootstrap.servers", system.getProperty("kafka.server.hosts"));
		propProduct.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		propProduct.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		// Product
		propProduct.put("acks", system.getProperty("kafka.acks"));
		propProduct.put("retries", system.getProperty("kafka.retries"));
		propProduct.put("batch.size", 16384);
		propProduct.put("linger.ms", 1);
		propProduct.put("max.block.ms", system.getProperty("kafka.max.block"));
		propProduct.put("request.timeout.ms", system.getProperty("kafka.request.timeout"));
		propProduct.put("buffer.memory", 33554432);
		producer = new KafkaProducer<String, String>(propProduct);
	}

	/**
	 * spring bean destroy
	 */
	public void destroy()
	{
		logger.info("Shutting down KafkaMessageCenter");
		closeProduct();
		closeConsumer();
	}

	/**
	 * 最少消费一次
	 * 
	 * @param config
	 *            Kafka消费者配置信息
	 * @param topics
	 *            消费主题
	 * @param listener
	 *            消息监听器
	 * @throws MessageCenterException
	 */
	public static void startComsumerAtLeastOnce(Properties config, List<String> topics, IMessageListener listener)
		throws MessageCenterException
	{
		if (isChannelConsumerClosed())
		{
			logger.warn("消息中心通道关闭监听程序不启动!");
			return;
		}
		if (topics.isEmpty())
		{
			logger.warn("消息中心未配置监听主题!");
			return;
		}
		try
		{
			config.put("enable.auto.commit", "false");
			KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config);
			consumers.add(consumer);
			String[] topicStr = topics.toArray(new String[0]);
			logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			logger.info("                             {}                               ", Arrays.toString(topicStr));
			logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

			logger.info("消息中心启动监听程序！");
			consumer.subscribe(topics);
			KafkaConsumerAtLeastOnceThread th = new KafkaConsumerAtLeastOnceThread();
			th.setConsumer(consumer);
			th.setListener(listener);
			ExecutorService executor = Executors.newFixedThreadPool(1);
			executor.execute(th);
		}
		catch (Exception e)
		{
			throw new MessageCenterException("消息中心启动监听失败：" + e.getMessage());
		}
	}

	/**
	 * 最多消费一次
	 * 
	 * @param config
	 *            Kafka消费者配置信息
	 * @param topics
	 *            消费主题
	 * @param listener
	 *            消息监听器
	 * @throws MessageCenterException
	 */
	public static void startConsumerAtMostOnce(Properties config, List<String> topics, IMessageListener listener)
		throws MessageCenterException
	{
		if (isChannelConsumerClosed())
		{
			logger.warn("消息中心通道关闭监听程序不启动!");
			return;
		}
		if (topics.isEmpty())
		{
			logger.warn("消息中心未配置监听主题!");
			return;
		}
		try
		{
			config.put("enable.auto.commit", "true");
			KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config);
			consumers.add(consumer);
			String[] topicStr = topics.toArray(new String[0]);
			logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			logger.info("                             {}                               ", Arrays.toString(topicStr));
			logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

			logger.info("消息中心启动监听程序！");
			consumer.subscribe(topics);
			KafkaConsumerAtMostOnceThread th = new KafkaConsumerAtMostOnceThread();
			th.setConsumer(consumer);
			th.setListener(listener);
			ExecutorService executor = Executors.newFixedThreadPool(1);
			executor.execute(th);
		}
		catch (Exception e)
		{
			throw new MessageCenterException("消息中心启动监听失败：" + e.getMessage());
		}
	}

	/**
	 * 精确消费一次
	 * 
	 * @param config
	 *            Kafka消费者配置信息
	 * @param topics
	 *            消费主题
	 * @param listener
	 *            消息监听器
	 * @throws MessageCenterException
	 *             消息中间件异常
	 */
	public static void startConsumerExactlyOnce(Properties config, List<String> topics, IMessageListener listener)
		throws MessageCenterException
	{
		if (isChannelConsumerClosed())
		{
			logger.warn("消息中心通道关闭监听程序不启动!");
			return;
		}
		if (topics.isEmpty())
		{
			logger.warn("消息中心未配置监听主题!");
			return;
		}
		try
		{
			config.put("enable.auto.commit", "false");
			KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config);
			consumers.add(consumer);
			String[] topicStr = topics.toArray(new String[0]);
			logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			logger.info("                             {}                            ", Arrays.toString(topicStr));
			logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

			logger.info("消息中心启动监听程序,使用精确消费1次方式！");
			consumer.subscribe(topics, new KafkaConsumerRebalancerListener(consumer));

			ExecutorService executor = Executors.newFixedThreadPool(1);
			for (int i = 0; i < 10; i++)
			{
				KafkaConsumerExactlyOnceThread th = new KafkaConsumerExactlyOnceThread();
				th.setConsumer(consumer);
				th.setListener(listener);
				th.setOffsetManager(offsetManager);
				executor.execute(th);
				logger.info("启动消费者:{}", i);
			}
		}
		catch (Exception e)
		{
			throw new MessageCenterException("消息中心启动监听失败：" + e.getMessage());
		}
	}

	/**
	 * 启动同步消费者
	 * @param config 消费者配置
	 * @param topics 消息主题
	 * @param messageQueueConsumerService 消息处理实现
	 * @throws MessageCenterException
	 */
	public static void startSyncConsumer(Properties config, List<String> topics,
										 MessageQueueConsumer messageQueueConsumerService) throws MessageCenterException
	{
		if (isChannelConsumerClosed())
		{
			logger.warn("消息中心通道关闭监听程序不启动!");
			return;
		}
		if (topics.isEmpty())
		{
			logger.warn("消息中心未配置监听主题!");
			return;
		}
		try
		{
			config.put("enable.auto.commit", "false");
			KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config);
			consumers.add(consumer);
			String[] topicStr = topics.toArray(new String[0]);
			logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			logger.info("                             {}                            ", Arrays.toString(topicStr));
			logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

			logger.info("消息中心启动监听程序,使用同步精确消费方式！");
			consumer.subscribe(topics, new KafkaConsumerRebalancerListener(consumer));

			ExecutorService executor = Executors.newFixedThreadPool(1);

			KafkaSyncConsumerThread th = new KafkaSyncConsumerThread();
			th.setConsumer(consumer);
			th.setMessageQueueConsumer(messageQueueConsumerService);
			executor.execute(th);
			logger.info("启动同步消费者:{}", executor.getClass().getName());
		}
		catch (Exception e)
		{
			throw new MessageCenterException("消息中心启动监听失败：" + e.getMessage());
		}
	}

	/**
	 * 消息通道是否关闭
	 * 
	 * @return
	 */
	public static boolean isChannelClosed()
	{
		if ("1".equals(kafkaOpen))
		{
			return false;
		}
		return true;
	}

	/**
	 * 消息消费者是否关闭
	 * 
	 * @return
	 */
	public static boolean isChannelConsumerClosed()
	{
		if ("1".equals(kafkaConsumerOpen))
		{
			return false;
		}
		return true;
	}

	/**
	 * 关闭生产者
	 */
	public static void closeProduct()
	{
		logger.info("关闭所有生产者！");
		if (producer != null)
		{
			producer.close();
		}
	}

	/**
	 * 关闭消费者
	 */
	public static void closeConsumer()
	{
		logger.info("所有消费者停止消费数据！");
		keepRunning = false;
		try
		{
			Thread.sleep(2000);
		}
		catch (InterruptedException e)
		{
			logger.warn("所有消费者停止消费数据！");
		}

		logger.info("准备关闭所有消费者！");
		for (KafkaConsumer<String, String> consumer : consumers)
		{
			if (consumer != null)
			{
				logger.info("CloseConsumer:{}", consumer.listTopics());
				consumer.close();
			}
		}
	}

	@Override
	public void send(String topic, String key, String message) throws MessageCenterException
	{
		try
		{
			long s = System.currentTimeMillis();
			Future<RecordMetadata> rs = producer.send(new ProducerRecord<String, String>(topic, key, message));
			RecordMetadata rm = rs.get();
			if (rm != null)
			{
				logger.debug(
						"Kafka:ProducterRecord(topic = {}, partition = {}, offset = {}, key = {}, message = {}),{}ms",
						new Object[]
						{
							topic, rm.partition(), rm.offset(), key, message, System.currentTimeMillis() - s
						});
			}
		}
		catch (Exception e)
		{
			logger.error("消息发送失败，Kafka消息中心服务异常，原因:", e);
			throw new MessageCenterException(String.format("消息发送失败，Kafka消息中心服务异常，原因:%s", e.getMessage()));
		}
	}

	@Override
	public void sendMulti(String topic, String key, List<String> messages) throws MessageCenterException
	{
		try
		{
			for (String message : messages)
			{
				producer.send(new ProducerRecord<String, String>(topic, key, message));
			}
		}
		catch (Exception e)
		{
			logger.error("批量消息发送失败，Kafka消息中心服务异常，原因:", e);
			throw new MessageCenterException(String.format("批量消息发送失败，Kafka消息中心服务异常，原因:%s", e.getMessage()));
		}
	}
}
