package com.myframework.core.token.strategy.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myframework.core.token.exception.TokenException;
import com.myframework.core.token.TokenManager;
import com.myframework.core.token.strategy.StrategyEnum;
import com.myframework.core.token.strategy.TokenStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

/**
 * Created by zw on 2017/7/20.
 */
public class BaseTokenStrategy extends TokenStrategy {

    private final Log logger = LogFactory.getLog(this.getClass());


    @Override
    public StrategyEnum vaidateToken(String token, UserDetails userDetails, HttpServletRequest request, HttpServletResponse reponse)
            throws TokenException {

        if (!TokenManager.isTokenExpired(token)) {
            throw new TokenException("toke expired!");
        }
        
        // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
        // the database compellingly. Again it's up to you ;)
        if (TokenManager.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            logger.info("authenticated user " + userDetails.getUsername() + ", setting security context");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        return StrategyEnum.STRATEGY_VALIDATE_SUCCESS;

    }

    @Override
    public int getStrategyPriority() {
        return 5;
    }

}