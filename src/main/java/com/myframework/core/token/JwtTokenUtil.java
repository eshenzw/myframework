package com.myframework.core.token;

import com.myframework.core.filter.RequestFilter;
import com.myframework.util.CookieUtil;
import com.myframework.util.StringUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.mobile.device.Device;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    public boolean tokenEnable;

    private String tokenHeader;

    private String tokenPrefix;

    private String secret;

    private Long expiration;

    private String redirectUrl;

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

    private String generateAudience(Device device) {
        String audience = TokenConstant.AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = TokenConstant.AUDIENCE_WEB;
        } else if (device.isTablet()) {
            audience = TokenConstant.AUDIENCE_TABLET;
        } else if (device.isMobile()) {
            audience = TokenConstant.AUDIENCE_MOBILE;
        }
        return audience;
    }

    private Boolean ignoreTokenExpiration(String token) {
        String audience = getAudienceFromToken(token);
        return (TokenConstant.AUDIENCE_TABLET.equals(audience) || TokenConstant.AUDIENCE_MOBILE.equals(audience));
    }

    public String generateToken(JwtTokenInfo tokenInfo) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(TokenConstant.CLAIM_KEY_USERNAME, tokenInfo.getUsername());
        claims.put(TokenConstant.CLAIM_KEY_AUDIENCE, generateAudience(tokenInfo.getDevice()));
        claims.put(TokenConstant.CLAIM_KEY_GRANTED, StringUtil.arrayToDelimitedString(tokenInfo.getAuths().toArray(), ","));

        final Date createdDate = new Date();
        claims.put(TokenConstant.CLAIM_KEY_CREATED, createdDate);

        return doGenerateToken(tokenInfo.getUid(), claims);
    }

    private String doGenerateToken(String subject, Map<String, Object> claims) {
        final Date createdDate = (Date) claims.get(TokenConstant.CLAIM_KEY_CREATED);
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);

        System.out.println("doGenerateToken " + createdDate);

        String token = Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        if (getTokenPrefix() != null) {
            token = new StringBuilder(getTokenPrefix()).append(" ").append(token).toString();
        }
        // 把当前token放到cookie中去
        if (RequestFilter.getRequest() != null && RequestFilter.getResponse() != null) {
            CookieUtil.setCookie(getTokenHeader(), token, getExpiration().intValue(), RequestFilter.getRequest(), RequestFilter.getResponse());
        }
        return token;
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(TokenConstant.CLAIM_KEY_CREATED, new Date());
            refreshedToken = doGenerateToken(claims.getSubject(), claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, JwtTokenInfo tokenInfo) {
        final String username = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        //final Date expiration = getExpirationDateFromToken(token);
        return (
                username.equals(tokenInfo.getUsername())
                        && !isTokenExpired(token)
                        && !isCreatedBeforeLastPasswordReset(created, tokenInfo.getLastPasswordReset()));
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
}