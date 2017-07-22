package com.myframework.core.token.strategy;

import com.myframework.exception.TokenException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zw on 2017/7/20.
 */
public interface Strategy
{

	/**
	 * token校验策略
	 * 
	 * @param request
	 * @param reponse
	 * @return 
	 */
	StrategyEnum vaidateToken(String token, HttpServletRequest request, HttpServletResponse reponse) throws TokenException;

	/**
	 * 获取策略的优先级，优先级高的优先执行.优先级从 0-10.10 级最优先执行 从性能最优考虑，建议: 1. 白名单校验放 10级 2.
	 * 默认的token是否过期的校验已经放在5级，这个由系统默认提供了 3. 业务的逻辑校验可以放在5级以下
	 * 
	 * @return
	 */
	int getStrategyPriority();

}