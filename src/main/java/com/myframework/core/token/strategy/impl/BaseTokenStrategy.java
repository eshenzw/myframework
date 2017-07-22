package com.myframework.core.token.strategy.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myframework.exception.TokenException;
import com.myframework.core.token.TokenManager;
import com.myframework.core.token.strategy.StrategyEnum;
import com.myframework.core.token.strategy.TokenStrategy;

/**
 * Created by zw on 2017/7/20.
 */
public class BaseTokenStrategy extends TokenStrategy {

    @Override
    public StrategyEnum vaidateToken(String token, HttpServletRequest request, HttpServletResponse reponse)
            throws TokenException {

        if (!TokenManager.isTokenExpired(token)) {
            throw new TokenException("toke expired!");
        }
        return StrategyEnum.STRATEGY_VALIDATE_SUCCESS;

    }

    @Override
    public int getStrategyPriority() {
        return 5;
    }

}