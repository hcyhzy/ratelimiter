package com.hc.ratelimiter;

import com.hc.ratelimiter.env.redis.RedisConfig;
import com.hc.ratelimiter.rule.MapRateLimitRule;
import com.hc.ratelimiter.rule.RateLimitRule;
import com.hc.ratelimiter.rule.config.TokenBucketLimitConfig;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author hc
 **/
public class DistributedTokenBucketRateLimiterTest {

    private RateLimiterInterface rateLimiterInterface;

    private static final String limitKey = "/counter";

    @BeforeMethod
    public void setUp() {
        RateLimitRule rateLimitRule = new MapRateLimitRule();
        TokenBucketLimitConfig limitConfig = new TokenBucketLimitConfig();
        limitConfig.setMaxPermits(100);
        rateLimitRule.addLimitConfig(limitKey, limitConfig);

        RedisConfig redisConfig = new RedisConfig("127.0.0.1", 6379);

        rateLimiterInterface = new DistributedTokenBucketRateLimiter
                .Builder()
                .rateLimitRule(rateLimitRule)
                .redisConfig(redisConfig)
                .build();

        // 测试配置修改
//        new Thread(() -> {
//            try {
//                Thread.sleep(2000);
//                limitConfig.setMaxPermits(10);
//                rateLimitRule.addLimitConfig("/xxxxx", limitConfig);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
    }

    @Test
    public void testLimit() {
        long start = System.currentTimeMillis();
        try {
            rateLimiterInterface.limit(limitKey);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("time use :" + (System.currentTimeMillis() - start));
        }
    }

    @Test
    public void pTestLimit() {
        long start = System.currentTimeMillis();
        try {
            for (int i = 0; i < 1000; i++) {
                rateLimiterInterface.limit(limitKey);
                System.out.println(i);
                if (i % 100 == 0) {
                    System.out.println("time use :" + (System.currentTimeMillis() - start));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("time use :" + (System.currentTimeMillis() - start));
        }
    }
}