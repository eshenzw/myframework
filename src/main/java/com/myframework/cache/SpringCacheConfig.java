package com.myframework.cache;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;

import com.google.common.cache.CacheBuilder;

/**
 * Cache配置類，用于缓存方法返回的数据
 * Created by zhaow on 2015/9/9.
 */
@Configuration
@EnableCaching(proxyTargetClass = true)
public class SpringCacheConfig implements CachingConfigurer {
    public static final int DEFAULT_MAXSIZE = 50000;
    public static final int DEFAULT_TTL = 10;

    /**
     * 定義cache名稱、超時時長（秒）、最大size
     * 每个cache缺省10秒超时、最多缓存50000条数据，需要修改可以在构造方法的参数中指定。
     */
    public enum Caches{
        commonCache(2*60*60);

        Caches() {
        }

        Caches(int ttl) {
            this.ttl = ttl;
        }

        Caches(int ttl, int maxSize) {
            this.ttl = ttl;
            this.maxSize = maxSize;
        }

        private int maxSize=DEFAULT_MAXSIZE;    //最大數量
        private int ttl=DEFAULT_TTL;        //过期时间（秒）

        public int getMaxSize() {
            return maxSize;
        }
        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }
        public int getTtl() {
            return ttl;
        }
        public void setTtl(int ttl) {
            this.ttl = ttl;
        }
    }

    @Bean
    @Primary
    @Override
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        //把各个cache注册到cacheManager中，ConcurrentMapCache实现了org.springframework.cache.Cache接口
        ArrayList<ConcurrentMapCache> caches = new ArrayList<ConcurrentMapCache>();
        for(Caches c : Caches.values()){
            caches.add(new ConcurrentMapCache(c.name(),new ConcurrentHashMap(c.getMaxSize()),false));
        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    /**
     * 创建基于guava的Cache Manager
     * @return
     */
    @Bean
    public CacheManager guavaCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        //把各个cache注册到cacheManager中，GuavaCache实现了org.springframework.cache.Cache接口
        ArrayList<GuavaCache> caches = new ArrayList<GuavaCache>();
        for(Caches c : Caches.values()){
            caches.add(new GuavaCache(c.name(), CacheBuilder.newBuilder().recordStats().expireAfterWrite(c.getTtl(), TimeUnit.SECONDS).maximumSize(c.getMaxSize()).build()));
        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    /**
     * 创建基于ehcache的Cache Manager
     * @return
     */
    @Bean
    public CacheManager ehcacheCacheManager() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("config/ehcache.xml"));
        ehCacheManagerFactoryBean.setShared(true);
        return new EhCacheCacheManager(ehCacheManagerFactoryBean.getObject());
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建基于redis的Cache Manager
     * @return
     */
    @Bean
    public CacheManager redisCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        ArrayList<RedisCache> caches = new ArrayList<RedisCache>();

        //把各个cache注册到cacheManager中，RedisCache实现了org.springframework.cache.Cache接口
        for(Caches c: Caches.values()){
            caches.add(new RedisCache(redisTemplate, c.name(), c.getTtl()));
        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean
    public KeyGenerator myKeyGernertor(){
        return new MyKeyGenerator();
    }
}
