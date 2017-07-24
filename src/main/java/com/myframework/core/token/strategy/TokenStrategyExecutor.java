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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.CollectionUtils;

/**
 * Created by zw on 2017/7/20.
 */
public class TokenStrategyExecutor implements ApplicationContextAware {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private JwtTokenUtil jwtTokenUtil;

    private UserDetailsService userDetailsService;

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

        if (StringUtils.isEmpty(token)) {
            return;
        }

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
        String username = jwtTokenUtil.getUsernameFromToken(token);

        logger.info("checking authentication für user " + username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // It is not compelling necessary to load the use details from the database. You could also store the information
            // in the token and read it from it. It's up to you ;)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
            // the database compellingly. Again it's up to you ;)
            if (TokenManager.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authenticated user " + userDetails.getUsername() + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            for (TokenStrategy strategy : strategyList) {

                StrategyEnum result = strategy.vaidateToken(token, userDetails, request, reponse);

                if (result == StrategyEnum.STRATEGY_VALIDATE_SUCCESS_ADN_BREAK) {
                    break;

                }

            }
        }

    }

    public JwtTokenUtil getJwtTokenUtil() {
        return jwtTokenUtil;
    }

    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}