package com.myframework.core.token;

import com.myframework.core.filter.RequestFilter;
import com.myframework.core.token.annotation.IgnoreTokenAuth;
import com.myframework.core.token.annotation.WithTokenAuth;
import com.myframework.core.token.exception.TokenException;
import com.myframework.core.token.strategy.TokenStrategyExecutor;
import com.myframework.util.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限(Token)验证
 * Created by zw on 2017/9/11.
 */
public class TokenAuthInterceptor extends HandlerInterceptorAdapter {

    private final Log logger = LogFactory.getLog(TokenAuthInterceptor.class);

    @Autowired
    private TokenStrategyExecutor tokenStrategyExecutor;

    public TokenStrategyExecutor getTokenStrategyExecutor() {
        return tokenStrategyExecutor;
    }

    public void setTokenStrategyExecutor(TokenStrategyExecutor tokenStrategyExecutor) {
        this.tokenStrategyExecutor = tokenStrategyExecutor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean go = true;
        if (TokenManager.isTokenEnable()) {
            IgnoreTokenAuth ignoreTokenAuth = null;
            WithTokenAuth withTokenAuth = null;
            if (handler instanceof HandlerMethod) {
                ignoreTokenAuth = ((HandlerMethod) handler).getMethodAnnotation(IgnoreTokenAuth.class);
                withTokenAuth = ((HandlerMethod) handler).getMethodAnnotation(WithTokenAuth.class);
            }

            //如果有@IgnoreTokenAuth注解，则不验证token
            if (ignoreTokenAuth == null) {
                go = enableTokenValidate(request, response);
            }

            if (withTokenAuth != null) {
                go = enableTokenAuth(withTokenAuth.value(), request, response);
            }

        } else {
            go = super.preHandle(request, response, handler);
        }
        return go;
    }

    /**
     * 决定是否开启token校验
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private boolean enableTokenValidate(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        try {
            // 校验token
            tokenStrategyExecutor.tokenValidate((HttpServletRequest) request, (HttpServletResponse) response);

        } catch (TokenException e) {

            logger.info("SessionFilter token 校验失败,biz code :" + e.getCode() + ",message :" + e.getMessage(), e);
            // 对异常信息做处理
            redirectTo((HttpServletRequest) request, (HttpServletResponse) response, e);

            return false;

        } catch (Exception e) {
            logger.error("SessionFilter token 校验失败，非预期异常,message : " + e.getMessage(), e);
            redirectTo((HttpServletRequest) request, (HttpServletResponse) response,
                    new TokenException(e, "token校验非预期异常"));

            return false;

        }

        return true;

    }

    /**
     * 决定是否开启token 权限校验
     *
     * @param withTokenAuth
     * @param request
     * @param response
     * @return
     */
    private boolean enableTokenAuth(String withTokenAuth, ServletRequest request, ServletResponse response) throws IOException {
        try {
            TokenManager.validateTokenAuth(withTokenAuth, (HttpServletRequest) request);
        } catch (TokenException e) {

            logger.info("SessionFilter token 校验失败,biz code :" + e.getCode() + ",message :" + e.getMessage(), e);
            // 对异常信息做处理
            redirectTo((HttpServletRequest) request, (HttpServletResponse) response, e);

            return false;

        } catch (Exception e) {
            logger.error("SessionFilter token 校验失败，非预期异常,message : " + e.getMessage(), e);
            redirectTo((HttpServletRequest) request, (HttpServletResponse) response,
                    new TokenException(e, "token校验非预期异常"));

            return false;

        }
        return true;
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

        redirectTo(request, response, e, TokenDevice.UNKNOWN);

    }

    protected void redirectTo(HttpServletRequest request, HttpServletResponse response, TokenException e,
                              String audience) throws IOException {
        ITokenValidRedirect irediret = null;
        try {
            irediret = SpringContextUtil.getBean(ITokenValidRedirect.class);
        } catch (Exception e1) {
            logger.info("应用没有实现 ITokenValidRedirect，进行默认跳转操作 ");
        }
        if (irediret != null) {
            // 可在应用注入该bean实现类，进行自定义跳转
            irediret.redirect(request, response, e, audience);
        } else {
            // 提供redirect 默认跳转实现
            TokenDevice device = new TokenDevice(audience);
            if (device.isNormal()) {
                response.sendRedirect(RequestFilter.getBasePath() + TokenManager.getJwtTokenUtil().getRedirectUrl());
            } else if (device.isMobile()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Mobile Unauthorized:" + e.getMessage());
            } else if (device.isTablet()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Tablet Unauthorized:" + e.getMessage());
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unknown Unauthorized:" + e.getMessage());
            }
        }
    }
}
