package com.hc.ratelimiter;

import com.hc.ratelimiter.algo.RateLimiter;
import com.hc.ratelimiter.algo.cluster.DistributedTokenBucketAlgo;
import com.hc.ratelimiter.env.redis.RedisConfig;
import com.hc.ratelimiter.env.redis.RedisUtil;
import com.hc.ratelimiter.exception.RateLimiterException;
import com.hc.ratelimiter.rule.RateLimitRule;
import com.hc.ratelimiter.rule.config.LimitConfig;
import com.hc.ratelimiter.rule.config.TokenBucketLimitConfig;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

/**
 * 令牌桶实现类
 *
 * @author hc
 **/
public class DistributedTokenBucketRateLimiter extends AbstractRateLimiter {

    private RedisTemplate<String, Serializable> redisTemplate;

    private DistributedTokenBucketRateLimiter(Builder builder) throws RateLimiterException {
        super(builder.rateLimitRule);
        if (null == builder.redisConfig) {
            throw new RateLimiterException("redis config is null");
        }
        this.redisTemplate = RedisUtil.initRedis(builder.redisConfig);
    }


    @Override
    protected RateLimiter getRateLimiterAlgo(String limitKey, LimitConfig limitConfig) throws RateLimiterException {
        if (!(limitConfig instanceof TokenBucketLimitConfig)) {
            throw new RateLimiterException("limitConfig not instanceof TokenBucketLimitConfig");
        }
        return new DistributedTokenBucketAlgo(limitKey, redisTemplate, limitConfig);
    }

    public static final class Builder {
        private RedisConfig redisConfig;

        private RateLimitRule rateLimitRule;

        public Builder() {
        }

        public Builder redisConfig(RedisConfig val) {
            redisConfig = val;
            return this;
        }

        public Builder rateLimitRule(RateLimitRule val) {
            rateLimitRule = val;
            return this;
        }

        public DistributedTokenBucketRateLimiter build() {
            return new DistributedTokenBucketRateLimiter(this);
        }
    }
}
