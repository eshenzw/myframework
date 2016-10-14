package com.myframework.cache;

import com.myframework.util.SpringBeanManager;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * Created by zw on 2015/09/14.
 */
public class CacheUtil
{
	public static Cache getLocalCache(){
		CacheManager cacheManager = (CacheManager)SpringBeanManager.getBean("guavaCacheManager");
		return (Cache) cacheManager.getCache(SpringCacheConfig.Caches.localCache.name());
	}

	public static Cache getShareCache(){
		CacheManager cacheManager = (CacheManager)SpringBeanManager.getBean("redisCacheManager");
		return (Cache) cacheManager.getCache(SpringCacheConfig.RedisCaches.shareCache.name());
	}
}
