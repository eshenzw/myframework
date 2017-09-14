package com.myframework.core.token;

import com.myframework.core.cache.CacheUtil;
import com.myframework.core.filter.RequestFilter;
import com.myframework.core.token.exception.TokenException;
import com.myframework.util.CookieUtil;
import com.myframework.util.StringUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mobile.device.Device;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    /*是否启用token*/
    public boolean tokenEnable;
    /*token header头*/
    private String tokenHeader;
    /*token 前缀*/
    private String tokenPrefix;
    /*token加密秘钥*/
    private String secret;
    /*token过期时间(单位：s)*/
    private Long expiration;
    /*token过期后允许refresh Token的保护期(单位：s)*/
    private Long expirationProtectTime;
    /*验证失败重定向地址*/
    private String redirectUrl;
    /*刷新token的过期时间(单位：s)*/
    private Long refreshTokenExpiration;

    public String getSubjectFromToken(String token) {
        String subject;
        try {
            final Claims claims = getClaimsFromToken(token);
            subject = (String) claims.getSubject();
        } catch (Exception e) {
            subject = null;
        }
        return subject;
    }

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = (String) claims.get(TokenConstant.CLAIM_KEY_USERNAME);
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(TokenConstant.CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = (String) claims.get(TokenConstant.CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    public String getGrantedAuthsFromToken(String token) {
        String auths;
        try {
            final Claims claims = getClaimsFromToken(token);
            auths = (String) claims.get(TokenConstant.CLAIM_KEY_GRANTED);
        } catch (Exception e) {
            auths = null;
        }
        return auths;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Boolean inTokenExpirationProtectTime(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return new Date().before(new Date(expiration.getTime() + expirationProtectTime * 1000));
    }

    public JwtInfo generateToken(JwtSubjectInfo tokenInfo) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(TokenConstant.CLAIM_KEY_USERNAME, tokenInfo.getUsername());
        claims.put(TokenConstant.CLAIM_KEY_AUDIENCE, tokenInfo.getDevice().getPlatform());
        if (tokenInfo.getAuths() != null) {
            claims.put(TokenConstant.CLAIM_KEY_GRANTED, StringUtil.arrayToDelimitedString(tokenInfo.getAuths().toArray(), ","));
        } else {
            claims.put(TokenConstant.CLAIM_KEY_GRANTED, "");
        }

        final Date createdDate = new Date();
        claims.put(TokenConstant.CLAIM_KEY_CREATED, createdDate);

        return doGenerateToken(tokenInfo.getUid(), claims);
    }

    private JwtInfo doGenerateToken(String subject, Map<String, Object> claims) {
        final Date createdDate = (Date) claims.get(TokenConstant.CLAIM_KEY_CREATED);
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);

        System.out.println("doGenerateToken " + createdDate);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setToken(token);
        jwtInfo.setCreatedTime(createdDate.getTime());
        jwtInfo.setExpireTime(expirationDate.getTime());
        // 把当前token放到session中去
        setToken2Session(token);
        setToken2Cookie(token);
        return jwtInfo;
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || inTokenExpirationProtectTime(token));
    }

    public JwtInfo refreshToken(String token, boolean isRefresh) throws TokenException {
        if (isTokenExpired(token)) {
            throw new TokenException("刷新token已过期");
        }
        final Claims claims = getClaimsFromToken(token);
        Date createdDate = new Date();
        claims.put(TokenConstant.CLAIM_KEY_CREATED, createdDate);
        final Date expirationDate = new Date(createdDate.getTime() + (isRefresh ? refreshTokenExpiration : expiration) * 1000);
        String newToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(claims.getSubject())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setToken(newToken);
        jwtInfo.setCreatedTime(createdDate.getTime());
        jwtInfo.setExpireTime(expirationDate.getTime());
        if (!isRefresh) {
            // 把当前token放到session中去
            setToken2Session(token);
            // 把当前token放到cookie中去
            setToken2Cookie(token);
        }
        return jwtInfo;
    }

    /**
     * 校验token的正确性
     *
     * @param token
     * @param tokenInfo
     * @return
     */
    public Boolean validateToken(String token, JwtSubjectInfo tokenInfo) {
        final String username = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        //final Date expiration = getExpirationDateFromToken(token);
        return (tokenInfo != null
                && username.equals(tokenInfo.getUsername())
                //&& !isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created, tokenInfo.getLastPasswordReset()));
    }

    /**
     * 将token放在session中
     *
     * @param token
     */
    public void setToken2Session(String token) {
        if (RequestFilter.getSession() != null) {
            RequestFilter.getSession().setAttribute(TokenConstant.SESSION_REFER_TOKEN, token);
        }
        CacheUtil.getShareCache().put(generateCacheKeyForToken(token), token);
    }

    /**
     * 将token放在cache中
     *
     * @param token
     */
    public void setToken2Cookie(String token) {
        if (getTokenPrefix() != null) {
            token = new StringBuilder(getTokenPrefix()).append(" ").append(token).toString();
        }
        // 把当前token放到cookie中去
        if (RequestFilter.getRequest() != null && RequestFilter.getResponse() != null) {
            CookieUtil.setCookie(getTokenHeader(), token, getExpiration().intValue(), RequestFilter.getRequest(), RequestFilter.getResponse());
        }
    }

    public String getTokenFromSession(String token) {
        String cacheToken = null;
        if (CacheUtil.getShareCache().get(generateCacheKeyForToken(token)) != null) {
            cacheToken = CacheUtil.getShareCache().get(generateCacheKeyForToken(token)).get().toString();
        }
        if (StringUtil.isEmpty(cacheToken) && RequestFilter.getSession() != null && RequestFilter.getSession().getAttribute(TokenConstant.SESSION_REFER_TOKEN) != null) {
            cacheToken = (String) RequestFilter.getSession().getAttribute(TokenConstant.SESSION_REFER_TOKEN);
        }
        return cacheToken;
    }

    private String generateCacheKeyForToken(String token) {
        String key = new StringBuilder("myframework-token-cache-").append(getClaimsFromToken(token).getSubject()).append("-").append(getAudienceFromToken(token)).toString();
        return key;
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public boolean isTokenEnable() {
        return tokenEnable;
    }

    public void setTokenEnable(boolean tokenEnable) {
        this.tokenEnable = tokenEnable;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Long getExpirationProtectTime() {
        return expirationProtectTime;
    }

    public void setExpirationProtectTime(Long expirationProtectTime) {
        this.expirationProtectTime = expirationProtectTime;
    }

    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public void setRefreshTokenExpiration(Long refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

}