package com.hc.ratelimiter.algo.cluster;

import com.hc.ratelimiter.algo.RateLimiter;
import com.hc.ratelimiter.enumric.LuaResultEnum;
import com.hc.ratelimiter.exception.RateLimiterException;
import com.hc.ratelimiter.rule.config.LimitConfig;
import com.hc.ratelimiter.rule.config.SlidingWindowLimitConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分布式 : 计数器(滑动窗口限流)
 * <p>
 * 解决固定窗口模式下的尖刺问题
 *
 * @author hc
 **/
public class DistributedSlidingWindowAlgo implements RateLimiter {

    private String limitKey;

    private RedisTemplate<String, Serializable> redisTemplate;

    private static final DefaultRedisScript<Long> REDIS_SCRIPT;

    private SlidingWindowLimitConfig slidingWindowLimitConfig;


    static {
        REDIS_SCRIPT = new DefaultRedisScript<>();
        REDIS_SCRIPT.setResultType(Long.class);
        REDIS_SCRIPT.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/redis_sliding_window.lua")));
    }

    public DistributedSlidingWindowAlgo(String limitKey,
                                        RedisTemplate<String, Serializable> redisTemplate,
                                        LimitConfig limitConfig) throws RateLimiterException {
        this.limitKey = limitKey;
        this.redisTemplate = redisTemplate;
        this.slidingWindowLimitConfig = (SlidingWindowLimitConfig) limitConfig;
    }


    @Override
    public boolean tryAcquire() throws RateLimiterException {
        try {
            List<String> keys = Collections.singletonList(limitKey);
            Long result = redisTemplate.execute(REDIS_SCRIPT, keys,
                    slidingWindowLimitConfig.getMaxPermits(),
                    slidingWindowLimitConfig.getTimeUnit(),
                    slidingWindowLimitConfig.getWindowSize());
            return LuaResultEnum.FAIL.getResult().equals(result);
        } catch (Exception e) {
            throw new RateLimiterException(e.getMessage());
        }
    }
}
