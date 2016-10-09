package com.myframework.esb.kafka;

import com.myframework.util.StringUtil;

public class KafkaOffsetManager
{
	private String storagePrefix;

	public KafkaOffsetManager(String storagePrefix)
	{
		this.storagePrefix = storagePrefix;
	}

	/**
	 * 
	 * Overwrite the offset for the topic in an external storage.
	 * 
	 * 
	 * 
	 * @param topic
	 *            - Topic name.
	 * 
	 * @param partition
	 *            - Partition of the topic.
	 * 
	 * @param offset
	 *            - offset to be stored.
	 */

	public void saveOffsetInExternalStore(String topic, int partition, long offset) throws Exception
	{

		ZookeeperManager.save(getStoragePath(topic, partition), String.valueOf(offset));

	}

	/**
	 * 
	 * @return he last offset + 1 for the provided topic and partition.
	 */

	public long readOffsetFromExternalStore(String topic, int partition) throws Exception
	{
		String offset = ZookeeperManager.read(getStoragePath(topic, partition));
		if (offset == null)
		{
			offset = ZookeeperManager.read(getStoragePath(topic, partition));
		}
		if (offset == null)
		{
			throw new Exception(String.format("Topic:%s,Partition:%s 对应的Offset节点不存在:%s", topic, partition,
					getStoragePath(topic, partition)));
		}
		return StringUtil.toLong(offset) + 1;
	}

	private String getStoragePath(String topic, int partition)
	{
		return String.format("/myframework/kafka/%s-%s-%s", storagePrefix, topic, partition);
		// return storagePrefix + "-" + topic + "-" + partition;
	}
}
