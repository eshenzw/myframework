package com.myframework.esb.kafka;

import java.util.Collection;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaInnerConsumerRebalancerListener implements ConsumerRebalanceListener
{
	private static Logger logger = LoggerFactory.getLogger(KafkaInnerConsumerRebalancerListener.class);
	private KafkaOffsetManager offsetManager = new KafkaOffsetManager("offset");

	private Consumer<String, String> consumer;

	public KafkaInnerConsumerRebalancerListener(Consumer<String, String> consumer)
	{
		this.consumer = consumer;
	}

	/**
	 * Rebalance 触发之前
	 */
	@Override
	public void onPartitionsRevoked(Collection<TopicPartition> partitions)
	{
		logger.warn("Kafka:ReBalance...............................Before");
		consumer.commitSync();
	}

	/**
	 * Rebalance 完成之后
	 */
	@Override
	public void onPartitionsAssigned(Collection<TopicPartition> partitions)
	{
		logger.warn("Kafka:ReBalanceAfter...............................After");
		for (TopicPartition partition : partitions)
		{
			OffsetAndMetadata offset = consumer.committed(partition);
			logger.warn("Kafka:ReBalanceAfter topic= {}, partition = {}, offset = {}", new Object[]
			{
				partition.topic(), partition.partition(), offset.offset()
			});
			consumer.seek(partition, offset.offset());

		}
	}

}
