package com.myframework.core.token;

import com.myframework.core.token.exception.TokenException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zw on 2017/7/24.
 */
public interface ITokenValidRedirect {
    public void redirect(HttpServletRequest request, HttpServletResponse response, TokenException e,
                                   String audience);
}
