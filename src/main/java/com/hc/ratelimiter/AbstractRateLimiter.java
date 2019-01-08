package com.hc.ratelimiter;

import com.hc.ratelimiter.algo.RateLimiter;
import com.hc.ratelimiter.exception.OverloadException;
import com.hc.ratelimiter.exception.RateLimiterException;
import com.hc.ratelimiter.rule.RateLimitRule;
import com.hc.ratelimiter.rule.config.LimitConfig;

/**
 * 限流抽象类 : 调用不同的限流算法
 *
 * @author hc
 **/
public abstract class AbstractRateLimiter implements RateLimiterInterface {

    private RateLimitRule rateLimitRule;

    AbstractRateLimiter(RateLimitRule rateLimitRule) {
        this.rateLimitRule = rateLimitRule;
    }

    @Override
    public void limit(String limitKey) throws OverloadException, RateLimiterException {

        if (null == rateLimitRule) {
            // no limit rule, success
            return;
        }

        LimitConfig limitConfig = rateLimitRule.getLimitConfig(limitKey);
        if (null == limitConfig) {
            // no limit rule, success
            return;
        }

        RateLimiter rateLimiter = getRateLimiterAlgo(limitKey, limitConfig);
        boolean result = rateLimiter.tryAcquire();
        if (result) {
            // 限流 : 抛出异常
            throw new OverloadException();
        }
    }

    protected abstract RateLimiter getRateLimiterAlgo(String limitKey, LimitConfig limitConfig)
            throws RateLimiterException;
}
