package com.myframework.core.common.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * 随机选择策略
 * 
 */
public class RandomStrategy implements ChoiceStrategy {
    private final Logger logger = LoggerFactory.getLogger(RandomStrategy.class);

    private final Random random =   new Random();

    @Override
    public <T> T getInstance(List<T> all) {
        if(all.size() == 0) {
            return null;
        } else {
            int thisIndex = random.nextInt(all.size());

            logger.debug("choose index:{} from all: {}", thisIndex, all);
            return  all.get(thisIndex);
        }
    }
}
