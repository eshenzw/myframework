package com.myframework.core.token;

import com.myframework.core.filter.RequestFilter;
import com.myframework.core.token.exception.TokenException;
import com.myframework.core.token.strategy.TokenStrategyExecutor;
import com.myframework.util.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private TokenStrategyExecutor tokenStrategyExecutor;

    public TokenStrategyExecutor getTokenStrategyExecutor() {
        return tokenStrategyExecutor;
    }

    public void setTokenStrategyExecutor(TokenStrategyExecutor tokenStrategyExecutor) {
        this.tokenStrategyExecutor = tokenStrategyExecutor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            if (TokenManager.isTokenEnable()) {
                enableTokenValidate(request, response, chain);
            } else {
                chain.doFilter(request, response);
            }
        } finally {
            // 清理threadlocal
            clearThreadLocal();
        }
    }

    /**
     * 决定是否开启token校验，redis存储session的功能
     *
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    private void enableTokenValidate(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            // 校验token
            tokenStrategyExecutor.tokenValidate((HttpServletRequest) request, (HttpServletResponse) response);

        } catch (TokenException e) {

            logger.info("SessionFilter token 校验失败,biz code :" + e.getCode() + ",message :" + e.getMessage(), e);
            // 对异常信息做处理
            redirectTo((HttpServletRequest) request, (HttpServletResponse) response, e);

            return;

        } catch (Exception e) {
            logger.error("SessionFilter token 校验失败，非预期异常,message : " + e.getMessage(), e);
            redirectTo((HttpServletRequest) request, (HttpServletResponse) response,
                    new TokenException(e, "token校验非预期异常"));

            return;

        }

        chain.doFilter(request, response);

    }

    /**
     * @param request
     * @param response
     * @param e
     */
    protected void redirectTo(HttpServletRequest request, HttpServletResponse response, TokenException e)
            throws IOException {

        String token = RequestFilter.getCurrentToken();

        if (StringUtils.isNotEmpty(token)) {

            try {

                String audience = TokenManager.getJwtTokenUtil().getAudienceFromToken(token);

                redirectTo(request, response, e, audience);

                return;

            } catch (Exception e1) {
                logger.error("redirctToLoginPage token解析失败，非预期异常, message : " + e1.getMessage() + ",[token]=" + token,
                        e1);
            }

        }

        // 如果前面没能根据token 解析到 登录方式，下面尝试根据cookie中的值取下试试

        redirectTo(request, response, e, TokenConstant.AUDIENCE_UNKNOWN);

    }

    protected void redirectTo(HttpServletRequest request, HttpServletResponse response, TokenException e,
                              String audience) throws IOException {
        ITokenValidRedirect irediret = null;
        try {
            irediret = SpringContextUtil.getBean(ITokenValidRedirect.class);
        } catch (Exception e1){
            logger.info("应用没有实现 ITokenValidRedirect，进行默认跳转操作 ");
        }
        if (irediret != null) {
            // 可在应用注入该bean实现类，进行自定义跳转
            irediret.redirect(request, response, e, audience);
        } else {
            // 提供redirect 默认跳转实现
            if (TokenConstant.AUDIENCE_WEB.equals(audience)) {
                response.sendRedirect(RequestFilter.getBasePath() + TokenManager.getJwtTokenUtil().getRedirectUrl());
            } else if (TokenConstant.AUDIENCE_MOBILE.equals(audience)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            } else if (TokenConstant.AUDIENCE_MOBILE.equals(audience)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            } else if (TokenConstant.AUDIENCE_UNKNOWN.equals(audience)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }
        }
    }

    /**
     *
     */
    private void clearThreadLocal() {
        RequestFilter.clearThreadLocal();
    }
}