package com.sq.transportmanage.gateway.service.common.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.sq.transportmanage.gateway.service.common.cache.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther:admin
 * @Date: 2019/06/28 18:24
 * @Description:(初始化Redis多数据源配置)
 */
@SuppressWarnings("ALL")
@Configuration
public class RedisConfig {

    private final static Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    //-------------------------Redis 多数据源  init----------------------------------
    //多数据源时需要给定一个默认得redis集群数据源
    @Bean(name = "ncdsRedis")
    @Primary
    public RedisConnectionFactory firstRedisConnection(@Value("${spring.redis.password}") String password,
                                                       @Value("${spring.redis.pool.max-idle}")int maxIdle,
                                                       @Value("${spring.redis.pool.max-active}")int maxActive,
                                                       @Value("${spring.redis.cluster.nodes}")List<String> nodes){
        logger.info("************初始化scosRedis集群************");
        JedisConnectionFactory factory = new JedisConnectionFactory(new RedisClusterConfiguration(nodes));
        JedisPoolConfig config = (JedisPoolConfig) factory.getPoolConfig();
        factory.setPassword(password);
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxActive);
        logger.info("************初始化scosRedis集群结束************");
        return factory;
    }
    //-------------------------Redis 多数据源  init----------------------end------------

    //------------------------多实例按照名称注入----------------------------------------
    @Autowired
    @Qualifier(value = "ncdsRedis")
    private RedisConnectionFactory ncdsRedisConnectionFactory;

    //实例引用直接使用name注入=stringScosRedisTemplate  ObjectRedis实例
    @Bean(name = "ncdsRedisTemplate")
    @Primary
    public RedisTemplate<String,Object> redisTemplate(){
        return getRedisTemplate(ncdsRedisConnectionFactory);
    }

    //
    @Bean(name = "ncdsSerRedisTemplate")
    @Primary
    public RedisTemplate<String, Serializable> redisSerTemplate(){
        return getSerRedisTemplate(ncdsRedisConnectionFactory);
    }

    //实例引用直接使用name注入=stringScosRedisTemplate  StringRedis实例
    @Bean(name = "stringNcdsRedisTemplate")
    @Primary
    public StringRedisTemplate stringRedisTemplate(){
        return getStringRedisTemplate(ncdsRedisConnectionFactory);
    }

    private RedisTemplate<String,Object> getRedisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String,Object> template = new RedisTemplate<String,Object>();
        template.setConnectionFactory(connectionFactory);
        setSerializer(template);
        template.afterPropertiesSet();
        return template;
    }

    private RedisTemplate<String,Serializable> getSerRedisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String,Serializable> template = new RedisTemplate<String,Serializable>();
        template.setConnectionFactory(connectionFactory);
        setSerializerV1(template);
        template.afterPropertiesSet();
        return template;
    }


    private StringRedisTemplate getStringRedisTemplate(RedisConnectionFactory connectionFactory){
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        setStringSerializer(template);
        template.afterPropertiesSet();
        return template;
    }

    /**ncdsSerRedisTemplate
     * StringTemplate序列化
     * @param template
     */
    private void setStringSerializer(StringRedisTemplate template){
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer );
        template.setHashKeySerializer(stringSerializer );
        template.setHashValueSerializer(stringSerializer );
        template.afterPropertiesSet();
    }

    /***
     * 使用com.alibaba.fastjson进行序列化
     * @param template
     */
    private void setSerializer(RedisTemplate<String, Object> template) {
        RedisSerializer<Object> fastJsonSerializer = new FastJsonRedisSerializer<>(Object.class);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(fastJsonSerializer);
        template.setHashKeySerializer(fastJsonSerializer);
        template.setHashValueSerializer(fastJsonSerializer);
    }

    private void setSerializerV1(RedisTemplate<String, Serializable> template) {
        RedisSerializer serializationRedisSerializer = new JdkSerializationRedisSerializer();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializationRedisSerializer);
        template.setHashKeySerializer(serializationRedisSerializer);
        template.setHashValueSerializer(serializationRedisSerializer);
    }


    @Bean(name = "redisUtil")
    public RedisUtil redisUtil(StringRedisTemplate redisTemplate) {
        logger.info("RedisUtil注入成功！");
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }

}

