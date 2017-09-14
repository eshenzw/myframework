package com.myframework.core.token.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myframework.core.filter.RequestFilter;
import com.myframework.core.token.JwtTokenUtil;
import com.myframework.core.token.TokenManager;
import com.myframework.core.token.exception.TokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

/**
 * Created by zw on 2017/7/20.
 */
public class TokenStrategyExecutor implements ApplicationContextAware {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private JwtTokenUtil jwtTokenUtil;

    private List<TokenStrategy> strategyList = new ArrayList<TokenStrategy>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, TokenStrategy> strategys = applicationContext.getBeansOfType(TokenStrategy.class);

        if (strategys == null || strategys.isEmpty()) {
            return;
        }

        strategyList.addAll(strategys.values());

        Collections.sort(strategyList);
    }

    /**
     * token 校验
     *
     * @param request
     * @param reponse
     * @return
     */
    public void tokenValidate(HttpServletRequest request, HttpServletResponse reponse) throws TokenException {

        // 获取token，
        String token = TokenManager.getTokenFromRequest(request);

        RequestFilter.setCurrentToken(token);

        try {

            doValidateChain(token, request, reponse);

        } catch (TokenException e) {

            throw e;

        } catch (Exception e) {

            logger.error(
                    "TokenStrategyExecutor execute token validate unexpected failed!! message is :" + e.getMessage(), e);

            throw new TokenException("unkown exceptin");
        }

    }

    /**
     * @param token
     * @param request
     * @param reponse
     * @return
     */
    private void doValidateChain(String token, HttpServletRequest request, HttpServletResponse reponse)
            throws TokenException {

        if (CollectionUtils.isEmpty(strategyList)) {
            return;
        }

        // authToken.startsWith("Bearer ")
        // String authToken = header.substring(7);
        String subject = jwtTokenUtil.getSubjectFromToken(token);

        logger.info("checking authentication for subject " + subject);

        for (TokenStrategy strategy : strategyList) {

            StrategyEnum result = strategy.vaidateToken(token, request, reponse);

            if (result == StrategyEnum.STRATEGY_VALIDATE_SUCCESS_ADN_BREAK) {
                break;
            }

        }

    }

    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public JwtTokenUtil getJwtTokenUtil() {
        return jwtTokenUtil;
    }

}