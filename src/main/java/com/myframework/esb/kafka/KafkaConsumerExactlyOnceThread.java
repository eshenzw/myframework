package com.myframework.esb.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myframework.esb.IMessageListener;
import com.myframework.esb.MessageCenterException;
import com.myframework.esb.MessageCenterHandler;
import com.myframework.util.StringUtil;

public class KafkaConsumerExactlyOnceThread implements Runnable
{
	private static Logger logger = LoggerFactory.getLogger(KafkaConsumerExactlyOnceThread.class);

	private KafkaConsumer<String, String> consumer;
	private IMessageListener listener;
	private KafkaOffsetManager offsetManager;

	@Override
	public void run()
	{
		try
		{
			while (KafkaMessageCenter.keepRunning)
			{
				ConsumerRecords<String, String> records = consumer.poll(100);
				for (ConsumerRecord<String, String> record : records)
				{
					String key = StringUtil.valueOf(record.key());
					String value = StringUtil.valueOf(record.value());
					logger.info("Kafka:topic:{}, partition:{}, offset:{}", new Object[]
					{
						record.topic(), record.partition(), record.offset()
					});
					logger.debug("Kafka:{}", record.toString());
					try
					{
						if (!listener.fetch(key, value))
						{
							MessageCenterHandler.sendMessageByTopic(MessageCenterHandler.TOPIC_TODO,
									String.format("%s:%s", MessageCenterHandler.TOPIC_BLOG, key), value);
						}
						// AutoCommit=false
						offsetManager.saveOffsetInExternalStore(record.topic(), record.partition(), record.offset());
					}
					catch (Exception e)
					{
						logger.error("1消息处理失败:{},{}", new Object[]
						{
							key, value
						});
						logger.error("1消息处理失败:", e);
						try
						{
							MessageCenterHandler.sendMessageByTopic(MessageCenterHandler.TOPIC_TODO,
									String.format("%s:%s", MessageCenterHandler.TOPIC_BLOG, key), value);
						}
						catch (MessageCenterException e1)
						{
							e1.printStackTrace();
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(String.format("1消费者线程运行异常，原因: %s", e.getMessage()), e);
		}
		finally
		{
			logger.warn("消费者停止消费数据！");
		}
	}

	public KafkaConsumer<String, String> getConsumer()
	{
		return consumer;
	}

	public void setConsumer(KafkaConsumer<String, String> consumer)
	{
		this.consumer = consumer;
	}

	public IMessageListener getListener()
	{
		return listener;
	}

	public void setListener(IMessageListener listener)
	{
		this.listener = listener;
	}

	public KafkaOffsetManager getOffsetManager()
	{
		return offsetManager;
	}

	public void setOffsetManager(KafkaOffsetManager offsetManager)
	{
		this.offsetManager = offsetManager;
	}
}
