package com.myframework.esb.kafka;

import com.myframework.esb.MessageCenterHandler;
import com.myframework.esb.MessageQueueConsumer;
import com.myframework.util.StringUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaSyncConsumerThread implements Runnable
{
	private static Logger logger = LoggerFactory.getLogger(KafkaSyncConsumerThread.class);

	private KafkaConsumer<String, String> consumer;
	private MessageQueueConsumer messageQueueConsumer;

	public MessageQueueConsumer getMessageQueueConsumer()
	{
		return messageQueueConsumer;
	}

	public void setMessageQueueConsumer(MessageQueueConsumer messageQueueConsumer)
	{
		this.messageQueueConsumer = messageQueueConsumer;
	}

	@Override
	public void run()
	{
		try
		{
			MessageCenterHandler.init();
			while (KafkaMessageCenter.keepRunning)
			{
				// logger.info("Kafka:poll timeout:{}",
				// messageQueueConsumer.getPollTimeout());
				ConsumerRecords<String, String> records = consumer.poll(messageQueueConsumer.getPollTimeout());
				// logger.info("Kafka:poll size:{}", records.count());
				for (ConsumerRecord<String, String> record : records)
				{
					String key = StringUtil.valueOf(record.key());
					String value = StringUtil.valueOf(record.value());

					KafkaOffsetManager offsetManager = new KafkaOffsetManager("offset");

					long offsetSaved = offsetManager.readOffsetFromExternalStore(record.topic(), record.partition());
					if (record.offset() < offsetSaved)
					{
						logger.info("Kafka:topic:{}, partition:{}, offset:{} PASS", new Object[]
						{
							record.topic(), record.partition(), record.offset()
						});
						continue;
					}
					logger.info("Kafka:topic:{}, partition:{}, offset:{}", new Object[]
					{
						record.topic(), record.partition(), record.offset()
					});
					logger.debug("Kafka:{}", record.toString());

					offsetManager.setTopic(record.topic());
					offsetManager.setPartition(record.partition());
					offsetManager.setOffset(record.offset());

					try
					{
						messageQueueConsumer.setOffsetManager(offsetManager);
						messageQueueConsumer.process(key, value);
					}
					catch (Exception e)
					{
						logger.error("同步消息处理失败:{},{}", new Object[]
						{
							key, value
						});
						logger.error("同步消息处理失败:", e);
						try
						{
							MessageCenterHandler.sendMessageByTopic(MessageCenterHandler.TOPIC_TODO,
									String.format("%s:%s", MessageCenterHandler.TOPIC_BLOG, key), value);
						}
						catch (Exception e1)
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
}
