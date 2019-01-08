package com.hc.ratelimiter.algo;

import com.hc.ratelimiter.exception.RateLimiterException;

/**
 * 限流接口
 * @author hc
 **/
public interface RateLimiter {

    /**
     * 判断流量
     * @return 是否限流(true : 限流/ false : 不限流)
     * @throws RateLimiterException 代码程序异常
     */
    boolean tryAcquire() throws RateLimiterException;
}
