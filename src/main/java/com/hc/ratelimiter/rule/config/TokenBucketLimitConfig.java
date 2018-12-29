package com.hc.ratelimiter.rule.config;

import com.alibaba.fastjson.annotation.JSONField;
import com.hc.ratelimiter.exception.RateLimiterException;

/**
 * 令牌桶算法
 *
 * @author hc
 * @date 2018-04-28 11:19
 **/
public class TokenBucketLimitConfig extends LimitConfig {

    /**
     * 令牌间隔最大时间 : 1000ms
     */
    private static final int MAX_INTERVAL_MILLIS = 1000;

    /**
     * 令牌间隔最小时间 : 1ms
     */
    private static final int MIN_INTERVAL_MILLIS = 1;

    /**
     * 每次请求消耗资源数量
     */
    @JSONField(name = "use_limit")
    private int useLimit = 1;

    /**
     * 添加令牌的间隔频率(1s内分多少次添加令牌)
     * 默认给每秒分为10段
     */
    @JSONField(name = "interval_rate")
    private int intervalRate = 10;


    public TokenBucketLimitConfig() {
    }

    /**
     * 构造方法
     *
     * @param maxPermits 最大令牌数
     */
    public TokenBucketLimitConfig(int maxPermits) {
        this.setMaxPermits(maxPermits);
        this.changeInterval();
    }

    /**
     * 构造方法
     *
     * @param maxPermits   最大令牌数
     * @param intervalRate 令牌添加频率
     */
    public TokenBucketLimitConfig(int maxPermits, int intervalRate) {
        this.setMaxPermits(maxPermits);
        this.intervalRate = intervalRate;
        this.changeInterval();
    }

    private void changeInterval() {
        if (getMaxPermits() / this.intervalRate < 1) {
            this.setIntervalRate(MIN_INTERVAL_MILLIS);
        }
    }

    public int getUseLimit() {
        return useLimit;
    }

    public void setUseLimit(int useLimit) {
        this.useLimit = useLimit;
    }

    public int getIntervalRate() {
        return intervalRate;
    }

    public void setIntervalRate(int intervalRate) {
        if (intervalRate > MAX_INTERVAL_MILLIS) {
            throw new RateLimiterException("intervalRate must be less than 1000");
        }
        if (intervalRate < MIN_INTERVAL_MILLIS) {
            throw new RateLimiterException("intervalRate must be more than 1");
        }
        this.intervalRate = intervalRate;
    }
}
