package com.myframework.esb.kafka;

import java.util.Collection;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaConsumerRebalancerListener implements ConsumerRebalanceListener
{
	private static Logger logger = LoggerFactory.getLogger(KafkaConsumerRebalancerListener.class);
	private KafkaOffsetManager offsetManager = new KafkaOffsetManager("offset");

	private Consumer<String, String> consumer;

	public KafkaConsumerRebalancerListener(Consumer<String, String> consumer)
	{
		this.consumer = consumer;
	}

	@Override
	public void onPartitionsRevoked(Collection<TopicPartition> partitions)
	{
		logger.warn("Kafka:ReBalance...............................Before");
		for (TopicPartition partition : partitions)
		{
			// long offset = consumer.position(partition);
			logger.warn("Kafka:ReBalanceBefore topic= {}, partition = {}", new Object[]
			{
				partition.topic(), partition.partition()
			});
			// offset值仅在消费完成后保存
			// offsetManager.saveOffsetInExternalStore(partition.topic(),
			// partition.partition(), offset);
		}
	}

	@Override
	public void onPartitionsAssigned(Collection<TopicPartition> partitions)
	{
		logger.warn("Kafka:ReBalanceAfter...............................After");
		for (TopicPartition partition : partitions)
		{
			try
			{
				long offset = offsetManager.readOffsetFromExternalStore(partition.topic(), partition.partition());
				logger.warn("Kafka:ReBalanceAfter topic= {}, partition = {}, offset = {}", new Object[]
				{
					partition.topic(), partition.partition(), offset
				});
				consumer.seek(partition, offset);
			}
			catch (Exception e)
			{
				logger.warn("节点Rebalance出现异常，关闭消费者：{}", e);
				KafkaMessageCenter.closeConsumer();
			}

		}
	}

}
