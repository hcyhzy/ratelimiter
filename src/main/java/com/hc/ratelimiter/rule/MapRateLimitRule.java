package com.hc.ratelimiter.rule;

import com.hc.ratelimiter.exception.RateLimiterException;
import com.hc.ratelimiter.rule.config.LimitConfig;

import java.util.concurrent.ConcurrentHashMap;

/**
 * HASHMAP存储规则
 *
 * @author hc
 * @date 2018-04-28 12:06
 **/
public class MapRateLimitRule implements RateLimitRule {

    private volatile ConcurrentHashMap<String, LimitConfig> limitRules =
            new ConcurrentHashMap<>();

    @Override
    public void addLimitConfig(String limitKey, LimitConfig limitConfig) throws RateLimiterException {
        try {
            if (null == limitKey || "".equals(limitKey) || limitConfig == null) {
                return;
            }

            limitRules.put(limitKey, limitConfig);
        } catch (Exception e) {
            throw new RateLimiterException(e);
        }
    }

    @Override
    public void updateLimitConfig(String limitKey, LimitConfig limitConfig) throws RateLimiterException {
        try {
            if (null == limitKey || "".equals(limitKey) || limitConfig == null) {
                throw new RateLimiterException("key or config must not be null");
            }

            limitRules.put(limitKey, limitConfig);
        } catch (Exception e) {
            throw new RateLimiterException(e);
        }
    }

    @Override
    public void removeLimitConfig(String limitKey) throws RateLimiterException {
        try {
            if (null == limitKey || "".equals(limitKey)) {
                return;
            }

            limitRules.remove(limitKey);
        } catch (Exception e) {
            throw new RateLimiterException(e);
        }
    }

    @Override
    public LimitConfig getLimitConfig(String limitKey) throws RateLimiterException {
        try {
            if (null == limitKey || "".equals(limitKey)) {
                return null;
            }

            return limitRules.get(limitKey);
        } catch (Exception e) {
            throw new RateLimiterException(e);
        }
    }
}
