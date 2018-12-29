package com.hc.ratelimiter.env.redis;

import com.hc.ratelimiter.exception.RateLimiterException;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;
import java.time.Duration;

/**
 * Redis构造类
 *
 * @author hc
 * @date 2018-04-21 13:42
 **/
public class RedisUtil {

    public static RedisTemplate<String, Serializable> initRedis(RedisConfig redisConfig)
            throws RateLimiterException {
        try {
            // poolConfig
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(redisConfig.getRedisPoolConfig().getMaxIdle());
            jedisPoolConfig.setMaxTotal(redisConfig.getRedisPoolConfig().getMaxTotal());
            jedisPoolConfig.setMinIdle(redisConfig.getRedisPoolConfig().getMinIdle());
            jedisPoolConfig.setMaxWaitMillis(redisConfig.getRedisPoolConfig().getMaxWaitMillis());
            jedisPoolConfig.setTestOnBorrow(redisConfig.getRedisPoolConfig().isTestOnBorrow());

            // jedisConnectionFactory
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            redisStandaloneConfiguration.setHostName(redisConfig.getHost());
            redisStandaloneConfiguration.setPort(redisConfig.getPort());
            redisStandaloneConfiguration.setDatabase(0);
            if (null != redisConfig.getPassword() && !"".equals(redisConfig.getPassword())) {
                redisStandaloneConfiguration.setPassword(RedisPassword.of(redisConfig.getPassword()));
            }
            JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
            jedisClientConfiguration.connectTimeout(Duration.ofMillis(redisConfig.getConnectTimeOut()));
            jedisClientConfiguration.usePooling().poolConfig(jedisPoolConfig).build();
            JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration,
                    jedisClientConfiguration.build());

            // redisTemplate
            RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(factory);
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
            redisTemplate.afterPropertiesSet();
            return redisTemplate;
        } catch (Exception e) {
            throw new RateLimiterException(e.getMessage());
        }
    }
}
