package com.myframework.core.token.strategy;


/**
 * Created by zw on 2017/7/20.
 */
public abstract class TokenStrategy implements Strategy, Comparable<TokenStrategy> {

    @Override
    public int compareTo(TokenStrategy strategy) {

        int priority = getStrategyPriority();

        return strategy.getStrategyPriority() - priority;

    }


}