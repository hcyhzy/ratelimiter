package com.hc.ratelimiter.rule;

import com.hc.ratelimiter.exception.RateLimiterException;
import com.hc.ratelimiter.rule.config.LimitConfig;

/**
 * 限流规则对象
 *
 * @author hc
 **/
public interface RateLimitRule {

    /**
     * 添加限流配置规则
     *
     * @param limitKey    限流KEY
     * @param limitConfig 规则
     * @throws RateLimiterException 异常
     */
    void addLimitConfig(String limitKey, LimitConfig limitConfig) throws RateLimiterException;

    /**
     * 更新限流配置
     *
     * @param limitKey 限流KEY
     * @param limitConfig 规则配置
     * @throws RateLimiterException 异常
     */
    void updateLimitConfig(String limitKey, LimitConfig limitConfig) throws RateLimiterException;

    /**
     * 删除限流规则(若无限流规则, 则此KEY不做限流处理)
     *
     * @param limitKey 限流KEY
     */
    void removeLimitConfig(String limitKey) throws RateLimiterException;

    /**
     * 清空规则信息
     *
     * @throws RateLimiterException 异常
     */
    void clearLimitConfig() throws RateLimiterException;

    /**
     * 根据限流KEY获取对应限流规则配置
     * @param limitKey 限流唯一KEY
     * @return 规则配置
     * @throws RateLimiterException 异常
     */
    LimitConfig getLimitConfig(String limitKey) throws RateLimiterException;
}
