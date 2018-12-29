package com.hc.ratelimiter.rule.config;

import com.alibaba.fastjson.annotation.JSONField;
import com.hc.ratelimiter.exception.RateLimiterException;

/**
 * @author hc
 * @date 2018-04-28 11:19
 **/
public class LimitConfig {

    /**
     * 默认限流单位
     */
    private static final int DEFAULT_TIME_UNIT = 1000;

    /**
     * 单位时间内允许的最大限流
     */
    @JSONField(name = "max_permits")
    private int maxPermits = 1;

    /**
     * 限流单位 : 默认1000 ms
     */
    @JSONField(name = "time_unit")
    private int timeUnit = DEFAULT_TIME_UNIT;

    public static int getDefaultTimeUnit() {
        return DEFAULT_TIME_UNIT;
    }

    public int getMaxPermits() {
        return maxPermits;
    }

    public void setMaxPermits(int maxPermits) {
        if (maxPermits < 1) {
            throw new RateLimiterException("maxPermits must be more than 1");
        }
        this.maxPermits = maxPermits;
    }

    public int getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(int timeUnit) {
        this.timeUnit = timeUnit;
    }
}
