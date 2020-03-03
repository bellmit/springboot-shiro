package com.sq.transportmanage.gateway.service.common.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 基于REDIS而实现SHIRO的Cache实现类
 * @author zhaoyali
 */
public class RedisCache<K, V> implements Cache<K, V> {
    private static final Logger log = LoggerFactory.getLogger(RedisCache.class);
    private String cachename;              //缓存名称
    private RedisTemplate<String, Serializable>    redisTemplate;
    private int expireSeconds = 3600; //默认的有效期

	public RedisCache(String cachename, RedisTemplate<String, Serializable> redisTemplate, int expireSeconds) {
		super();
		this.cachename = cachename;
		this.redisTemplate = redisTemplate;
		this.expireSeconds = expireSeconds;
	}
	
    @SuppressWarnings("unchecked")
    @Override
	public V get(K key) throws CacheException {
        try {
        	return (V) redisTemplate.opsForValue().get(  cachename+key.toString() );
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * Puts an object into the cache.
     *
     * @param key   the key.
     * @param value the value.
     */
    @SuppressWarnings("unchecked")
    @Override
	public V put(K key, V value) throws CacheException {
        try {
       	 V previousValue = (V) redisTemplate.opsForValue().getAndSet(   cachename+key.toString(), (Serializable)value   );
       	 redisTemplate.expire( cachename+key , expireSeconds, TimeUnit.SECONDS);
       	 return previousValue;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * Removes the element which matches the key.
     *
     * <p>If no element matches, nothing is removed and no Exception is thrown.</p>
     *
     * @param key the key of the element to remove
     */
    @SuppressWarnings("unchecked")
    @Override
	public V remove(K key) throws CacheException {
        try {
        	V value =  (V) redisTemplate.opsForValue().get(  cachename+ key.toString() );
        	redisTemplate.delete(  cachename+ key.toString() );
        	return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * Removes all elements in the cache, but leaves the cache in a useable state.
     */
    @Override
    public void clear() throws CacheException {
        try {
        	Set<String> keys = redisTemplate.keys( cachename+"*" );
        	if(keys==null || keys.size()==0 ) {
                return ;
        	}
        	redisTemplate.delete(keys);
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public int size() {
        try {
        	Set<String> keys = redisTemplate.keys( cachename+"*" );
        	if(keys!=null) {
        		return keys.size();
        	}
            return 0;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
	public Set<K> keys() {
        try {
        	return (Set<K>) redisTemplate.keys( cachename+"*" );
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
	public Collection<V> values() {
        try {
        	Set<String> keys = redisTemplate.keys( cachename+"*" );
        	if(keys==null || keys.size()==0) {
                return Collections.emptyList();
        	}
            List<V> values = new ArrayList<V>(keys.size());
        	for(String key : keys ) {
            	V value = (V) redisTemplate.opsForValue().get(key);
        		if(value!=null) {
        			values.add(value);
        		}
        	}
            return Collections.unmodifiableList(values);
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }
}