package com.myframework.core.token.strategy;


/**
 * Created by zw on 2017/7/20.
 */
public abstract class TokenStrategy implements Strategy, Comparable<TokenStrategy> {

    /**
     * 获取策略的优先级，优先级高的优先执行.
     * @param strategy
     * @return
     */
    @Override
    public int compareTo(TokenStrategy strategy) {

        int priority = getStrategyPriority();

        return strategy.getStrategyPriority() - priority;

    }


}