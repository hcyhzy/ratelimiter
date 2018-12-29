package com.hc.ratelimiter.algo.cluster;

import com.hc.ratelimiter.algo.RateLimiter;
import com.hc.ratelimiter.enumric.LuaResultEnum;
import com.hc.ratelimiter.exception.RateLimiterException;
import com.hc.ratelimiter.rule.config.LimitConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分布式 : 计数器(固定窗口限流)
 *
 * 计数器模式容易产生请求毛刺, 不能使请求均匀进入
 * 时间差容易引起限流失效 : 比如限流是100, 在上一秒的结束和下一秒的开始, 连续请求200次, 此时按照要求是可以通过的.
 * 但是后端服务则可能承受1s内200的请求量
 *
 * @author hc
 * @date 2018-04-28 20:11
 **/
public class DistributedFixWindowAlgo implements RateLimiter {

    private String limitKey;

    private RedisTemplate<String, Serializable> redisTemplate;

    private static final DefaultRedisScript<Long> REDIS_SCRIPT;

    private LimitConfig limitConfig;


    static {
        REDIS_SCRIPT = new DefaultRedisScript<>();
        REDIS_SCRIPT.setResultType(Long.class);
        REDIS_SCRIPT.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/redis_fix_window.lua")));
    }

    public DistributedFixWindowAlgo(String limitKey,
                                    RedisTemplate<String, Serializable> redisTemplate,
                                    LimitConfig limitConfig) throws RateLimiterException {
        this.limitKey = limitKey;
        this.redisTemplate = redisTemplate;
        this.limitConfig = limitConfig;
    }


    @Override
    public boolean tryAcquire() throws RateLimiterException {
        try {
            List<String> keys = Collections.singletonList(limitKey);
            Long result = redisTemplate.execute(REDIS_SCRIPT, keys,
                    limitConfig.getMaxPermits(),
                    limitConfig.getTimeUnit());
            return LuaResultEnum.FAIL.getResult().equals(result);
        } catch (Exception e) {
            throw new RateLimiterException(e.getMessage());
        }
    }
}
