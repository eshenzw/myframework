package com.myframework.core.token;

import com.myframework.core.token.exception.TokenException;
import com.myframework.util.CookieUtil;
import com.myframework.util.SpringContextUtil;
import com.myframework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mobile.device.Device;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by zw on 2017/7/19.
 */
public class TokenManager {

    public static boolean isTokenEnable() {
        return getJwtTokenUtil().isTokenEnable();
    }

    public static String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(getJwtTokenUtil().getTokenHeader());
        //header 取不到的时候从参数里取token字段
        if (StringUtil.isEmpty(token)) {
            token = request.getParameter(getJwtTokenUtil().getTokenHeader());
        }
        //header 取不到的时候从token里取token字段
        if (StringUtil.isEmpty(token)) {
            token = CookieUtil.getCookie(request.getCookies(), getJwtTokenUtil().getTokenHeader()).getValue();
        }

        if (token.contains(getJwtTokenUtil().getTokenPrefix())) {
            token = token.substring(getJwtTokenUtil().getTokenPrefix().length()+1);
        }

        return token;
    }

    public static boolean isTokenExpired(String token) {
        return getJwtTokenUtil().isTokenExpired(token);
    }

    public static boolean validateToken(String token, JwtTokenInfo jwtTokenInfo) {
        return getJwtTokenUtil().validateToken(token, jwtTokenInfo);
    }

    public static boolean validateTokenAuth(String withTokenAuth, HttpServletRequest request) throws TokenException {
        String token = getTokenFromRequest(request);
        if (StringUtils.isEmpty(token)) {
            throw new TokenException("token不存在");
        }
        String grantedAuths = getJwtTokenUtil().getGrantedAuthsFromToken(token);
        if (StringUtil.isEmpty(grantedAuths)) {
            throw new TokenException("token授权失败");
        }
        Collection<String> granted = Arrays.asList(grantedAuths.split(","));
        Collection<String> auths = Arrays.asList(withTokenAuth.split(","));
        for (String auth : auths) {
            if (!granted.contains(auth)) {
                throw new TokenException("token授权失败");
            }
        }
        return true;
    }

    public static String createAuthenticationToken(JwtTokenInfo jwtTokenInfo,
                                                   Device device) {
        // Reload password post-security so we can generate token
        JwtTokenUtil jwtTokenUtil = SpringContextUtil.getBean(JwtTokenUtil.class);
        jwtTokenInfo.setDevice(device);
        final String token = jwtTokenUtil.generateToken(jwtTokenInfo);
        // Return the token
        return token;
    }

    public static String refreshAndGetAuthenticationToken(HttpServletRequest request, JwtTokenInfo jwtTokenInfo) {
        String token = getTokenFromRequest(request);
        String username = getJwtTokenUtil().getUsernameFromToken(token);

        if (getJwtTokenUtil().canTokenBeRefreshed(token, jwtTokenInfo.getLastPasswordReset())) {
            String refreshedToken = getJwtTokenUtil().refreshToken(token);
            return refreshedToken;
        } else {
            return null;
        }
    }

    public static JwtTokenUtil getJwtTokenUtil() {
        return (JwtTokenUtil) SpringContextUtil.getBean(JwtTokenUtil.class);
    }
}
