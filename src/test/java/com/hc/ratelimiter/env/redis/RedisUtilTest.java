package com.hc.ratelimiter.env.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.Serializable;

/**
 * @author hc
 * @date 2018-04-21 13:43
 **/
public class RedisUtilTest {

    private RedisConfig redisConfig;

    @BeforeMethod
    public void setUp() {
        redisConfig = new RedisConfig("127.0.0.1", 6379);
    }

    @Test
    public void testInitRedis() {
        RedisTemplate<String, Serializable> redisTemplate = RedisUtil.initRedis(redisConfig);
        System.out.println(redisTemplate.keys("*"));
    }
}