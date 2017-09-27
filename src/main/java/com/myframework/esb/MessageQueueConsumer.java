package com.myframework.esb;

import com.myframework.esb.kafka.KafkaOffsetManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

public abstract class MessageQueueConsumer {
    private static Logger logger = LoggerFactory.getLogger(MessageQueueConsumer.class);
    private KafkaOffsetManager offsetManager;
    private int pollTimeout = 500;

    public abstract boolean fetch(String key, String value);

    /**
     * 队列中消息处理失败时调用此方法,各子类可以覆盖此方法
     *
     * @param key   消息key
     * @param value 消息值
     */
    protected void fail(String key, String value) {
        logger.warn("队列中消息处理失败：[topic={},partition={},offset={},key={},value={}]", new Object[]
                {
                        offsetManager.getTopic(), offsetManager.getPartition(), offsetManager.getOffset(), key, value
                });
    }

    @Transactional(readOnly = false, rollbackFor = DataAccessException.class)
    public void process(String key, String value) throws Exception {
        if (fetch(key, value)) {
            offsetManager.saveOffsetInExternalStore();
        } else {
            fail(key, value);
        }
    }

    public KafkaOffsetManager getOffsetManager() {
        return offsetManager;
    }

    public void setOffsetManager(KafkaOffsetManager offsetManager) {
        this.offsetManager = offsetManager;
    }

    public int getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(int pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

}
