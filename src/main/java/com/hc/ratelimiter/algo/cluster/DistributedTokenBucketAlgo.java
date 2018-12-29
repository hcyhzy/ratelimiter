package com.hc.ratelimiter.algo.cluster;

import com.hc.ratelimiter.algo.RateLimiter;
import com.hc.ratelimiter.enumric.LuaResultEnum;
import com.hc.ratelimiter.exception.RateLimiterException;
import com.hc.ratelimiter.rule.config.LimitConfig;
import com.hc.ratelimiter.rule.config.TokenBucketLimitConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分布式 : 令牌桶算法
 *
 * 保证请求进入的匀速性
 *
 * @author hc
 * @date 2018-04-28 12:21
 **/
public class DistributedTokenBucketAlgo implements RateLimiter {

    private String limitKey;

    private RedisTemplate<String, Serializable> redisTemplate;

    private static final DefaultRedisScript<Long> REDIS_SCRIPT;

    private TokenBucketLimitConfig tokenBucketLimitConfig;


    static {
        REDIS_SCRIPT = new DefaultRedisScript<>();
        REDIS_SCRIPT.setResultType(Long.class);
        REDIS_SCRIPT.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/redis_token_bucket.lua")));
    }

    public DistributedTokenBucketAlgo(String limitKey,
                                      RedisTemplate<String, Serializable> redisTemplate,
                                      LimitConfig limitConfig) throws RateLimiterException {
        this.limitKey = limitKey;
        this.redisTemplate = redisTemplate;
        this.tokenBucketLimitConfig = (TokenBucketLimitConfig) limitConfig;
    }

    @Override
    public boolean tryAcquire() throws RateLimiterException {
        try {
            List<String> keys = Collections.singletonList(limitKey);
            Long result = redisTemplate.execute(REDIS_SCRIPT, keys,
                    tokenBucketLimitConfig.getUseLimit(),
                    System.currentTimeMillis(),
                    tokenBucketLimitConfig.getMaxPermits(),
                    tokenBucketLimitConfig.getIntervalRate());
            return LuaResultEnum.FAIL.getResult().equals(result);
        } catch (Exception e) {
            throw new RateLimiterException(e.getMessage());
        }
    }
}
