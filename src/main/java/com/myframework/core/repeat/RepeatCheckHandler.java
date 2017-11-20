package com.myframework.core.repeat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.myframework.util.StringUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author zw
 * @date 2017/11/20
 */
public class RepeatCheckHandler {
    public static final String SUBMIT_TOKEN = "__token__";

    private Cache<String, String> tokenCache = CacheBuilder.newBuilder()
            .initialCapacity(100)
            .maximumSize(2000)
            .concurrencyLevel(5)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public void saveToken(String token) {
        if (StringUtil.isNotEmpty(token)) {
            tokenCache.put(token, "1");
        }
    }

    public boolean isRepeat(String token) {
        String val = tokenCache.getIfPresent(token);
        if (val == null) {
            return false;
        }
        return true;
    }
}
