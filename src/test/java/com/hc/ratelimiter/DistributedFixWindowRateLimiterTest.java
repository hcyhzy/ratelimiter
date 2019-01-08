package com.hc.ratelimiter;

import com.hc.ratelimiter.env.redis.RedisConfig;
import com.hc.ratelimiter.rule.MapRateLimitRule;
import com.hc.ratelimiter.rule.RateLimitRule;
import com.hc.ratelimiter.rule.config.LimitConfig;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author hc
 **/
public class DistributedFixWindowRateLimiterTest {

    private RateLimiterInterface rateLimiterInterface;

    private static final String limitKey = "/counter";

    @BeforeMethod
    public void setUp() {
        RateLimitRule rateLimitRule = new MapRateLimitRule();
        LimitConfig limitConfig = new LimitConfig();
        limitConfig.setMaxPermits(100);
        rateLimitRule.addLimitConfig(limitKey, limitConfig);

        RedisConfig redisConfig = new RedisConfig("127.0.0.1", 6379);

        rateLimiterInterface = new DistributedFixWindowRateLimiter
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
                rateLimiterInterface.limit(limitKey);
                System.out.println(i);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("time use :" + (System.currentTimeMillis() - start));
        }
    }
}