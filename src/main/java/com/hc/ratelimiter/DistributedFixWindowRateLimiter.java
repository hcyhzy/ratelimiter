package com.hc.ratelimiter;

import com.hc.ratelimiter.algo.RateLimiter;
import com.hc.ratelimiter.algo.cluster.DistributedFixWindowAlgo;
import com.hc.ratelimiter.env.redis.RedisConfig;
import com.hc.ratelimiter.env.redis.RedisUtil;
import com.hc.ratelimiter.exception.RateLimiterException;
import com.hc.ratelimiter.rule.RateLimitRule;
import com.hc.ratelimiter.rule.config.LimitConfig;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

/**
 * 分布式 : 计数器(固定窗口
 *
 * @author hc
 **/
public class DistributedFixWindowRateLimiter extends AbstractRateLimiter {

    private RedisTemplate<String, Serializable> redisTemplate;

    private DistributedFixWindowRateLimiter(Builder builder) throws RateLimiterException {
        super(builder.rateLimitRule);
        if (null == builder.redisConfig) {
            throw new RateLimiterException("redis config is null");
        }
        this.redisTemplate = RedisUtil.initRedis(builder.redisConfig);
    }


    @Override
    protected RateLimiter getRateLimiterAlgo(String limitKey, LimitConfig limitConfig) throws RateLimiterException {
        return new DistributedFixWindowAlgo(limitKey, redisTemplate, limitConfig);
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

        public DistributedFixWindowRateLimiter build() {
            return new DistributedFixWindowRateLimiter(this);
        }
    }
}
