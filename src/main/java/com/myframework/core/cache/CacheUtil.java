package com.myframework.core.cache;

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
		if(cacheManager != null && cacheManager.getCacheNames().contains(SpringCacheConfig.Caches.localCache.name())){
			return (Cache) cacheManager.getCache(SpringCacheConfig.Caches.localCache.name());
		}else{
			return new EmptyCache();
		}
	}

	public static Cache getShareCache(){
		CacheManager cacheManager = (CacheManager)SpringBeanManager.getBean("redisCacheManager");
		if(cacheManager != null && cacheManager.getCacheNames().contains(SpringCacheConfig.RedisCaches.shareCache.name())) {
			return (Cache) cacheManager.getCache(SpringCacheConfig.RedisCaches.shareCache.name());
		}else{
			return new EmptyCache();
		}
	}
}
