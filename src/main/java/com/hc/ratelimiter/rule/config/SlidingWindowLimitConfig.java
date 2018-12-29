package com.hc.ratelimiter.rule.config;

import com.alibaba.fastjson.annotation.JSONField;
import com.hc.ratelimiter.exception.RateLimiterException;

/**
 * 计数器(滑动窗口)算法配置
 *
 * @author hc
 * @date 2018-04-28 11:19
 **/
public class SlidingWindowLimitConfig extends LimitConfig {


    /**
     * 默认1个窗口 : 则还是固定窗口
     */
    private static final int DEFAULT_WINDOW_SIZE = 1;

    /**
     * 窗口切分数量
     */
    @JSONField(name = "window_size")
    private int windowSize = DEFAULT_WINDOW_SIZE;


    public SlidingWindowLimitConfig() {
    }

    /**
     * 构造方法
     *
     * @param maxPermits 限流数量
     */
    public SlidingWindowLimitConfig(int maxPermits) {
        this.setMaxPermits(maxPermits);
    }

    /**
     * 构造方法
     *
     * @param maxPermits 限流数量
     * @param windowSize 窗口切分数量
     */
    public SlidingWindowLimitConfig(int maxPermits, int windowSize) {
        this.setMaxPermits(maxPermits);
        this.setWindowSize(windowSize);
    }

    /**
     * 构造方法
     *
     * @param maxPermits 限流数量
     * @param windowSize 窗口切分数量
     * @param timeUnit   限流时间单位
     */
    public SlidingWindowLimitConfig(int maxPermits, int windowSize, int timeUnit) {
        this.setMaxPermits(maxPermits);
        this.setTimeUnit(timeUnit);
        this.setWindowSize(windowSize);
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        if (windowSize < DEFAULT_WINDOW_SIZE) {
            throw new RateLimiterException("windowSize must be more than 1");
        }
        if (getMaxPermits() / windowSize <= 0) {
            throw new RateLimiterException("windowSize can not become sliding window");
        }
        this.windowSize = windowSize;
    }
}
