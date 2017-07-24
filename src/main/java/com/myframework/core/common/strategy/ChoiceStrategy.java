package com.myframework.core.common.strategy;

import java.util.List;


/**
 * 选择器,从库选择策略
 * 
 */
public interface ChoiceStrategy {

	public <T> T getInstance(List<T> slaves);

}
