package com.hc.ratelimiter;

import com.hc.ratelimiter.env.redis.RedisConfig;
import com.hc.ratelimiter.rule.MapRateLimitRule;
import com.hc.ratelimiter.rule.RateLimitRule;
import com.hc.ratelimiter.rule.config.SlidingWindowLimitConfig;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author hc
 **/
public class DistributedSlidingWindowRateLimiterTest {

    private RateLimiterInterface rateLimiterInterface;

    private static final String limitKey = "/counter";

    @BeforeMethod
    public void setUp() {
        RateLimitRule rateLimitRule = new MapRateLimitRule();
        SlidingWindowLimitConfig slidingWindowLimitConfig = new SlidingWindowLimitConfig();
        slidingWindowLimitConfig.setMaxPermits(100);
        slidingWindowLimitConfig.setWindowSize(10);
        rateLimitRule.addLimitConfig(limitKey, slidingWindowLimitConfig);

        RedisConfig redisConfig = new RedisConfig("127.0.0.1", 6379);

        rateLimiterInterface = new DistributedSlidingWindowRateLimiter
                .Builder()
                .rateLimitRule(rateLimitRule)
                .redisConfig(redisConfig)
                .build();

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
            for (int i = 0; i < 2000; i++) {
                try {
                    rateLimiterInterface.limit(limitKey);
                    System.out.println("success = " + i);
                    System.out.println("time use :" + (System.currentTimeMillis() - start));
                } catch (Exception e) {
                    System.out.println("error = " + i);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("time use :" + (System.currentTimeMillis() - start));
        }
    }
}