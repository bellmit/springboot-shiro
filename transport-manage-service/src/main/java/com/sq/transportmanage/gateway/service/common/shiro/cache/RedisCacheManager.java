package com.sq.transportmanage.gateway.service.common.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

/**
 * 基于REDIS而实现SHIRO的CacheManager实现类
 * @author zhaoyali
 */
public class RedisCacheManager implements CacheManager, Initializable, Destroyable {
    private static final Logger log = LoggerFactory.getLogger(RedisCacheManager.class);
    private RedisTemplate<String, Serializable>    redisTemplate;
    private int expireSeconds = 3600; //默认的有效期

	public RedisTemplate<String, Serializable> getRedisTemplate() {
		return redisTemplate;
	}
	public void setRedisTemplate(RedisTemplate<String, Serializable> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	public int getExpireSeconds() {
		return expireSeconds;
	}
	public void setExpireSeconds(int expireSeconds) {
		this.expireSeconds = expireSeconds;
	}


	@Override
    public final void init() throws CacheException {
    }
    @Override
    public void destroy() {
    }
    @Override
    public final <K, V> Cache<K, V> getCache(String name) throws CacheException {
    	return new RedisCache<K, V>( name, redisTemplate, expireSeconds);
    }
}