package com.myframework.core.token.strategy.impl;

import com.myframework.core.token.strategy.StrategyEnum;
import com.myframework.core.token.strategy.TokenStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zw on 2017/9/13.
 */
public class UrlTokenStrategy extends TokenStrategy {
    private static final Logger logger = LoggerFactory.getLogger(UrlTokenStrategy.class);
    private String[] whiteList;

    public void init() {
        logger.info("loading white uri.....");
        if (whiteList == null) {
            return;
        }
        for (int i = 0; i < whiteList.length; i++) {
            logger.info("请求URL白名单:{}", whiteList[i]);
        }
    }

    @Override
    public int getStrategyPriority() {
        return 50;
    }

    @Override
    public StrategyEnum vaidateToken(String token, HttpServletRequest request, HttpServletResponse reponse) {
        if (whiteList == null) {
            return StrategyEnum.STRATEGY_VALIDATE_SUCCESS;
        }
        // 统一白名单过滤
        String url = request.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (int i = 0; i < whiteList.length; i++) {
            String whiteUri = whiteList[i];
            if (url.indexOf(whiteUri) > -1 || pathMatcher.match(whiteUri, url)) {
                return StrategyEnum.STRATEGY_VALIDATE_SUCCESS_ADN_BREAK;
            }
        }

        return StrategyEnum.STRATEGY_VALIDATE_SUCCESS;
    }

    public String[] getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String[] whiteList) {
        this.whiteList = whiteList;
    }

}
