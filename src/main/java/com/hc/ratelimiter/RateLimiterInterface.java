package com.hc.ratelimiter;

import com.hc.ratelimiter.exception.OverloadException;
import com.hc.ratelimiter.exception.RateLimiterException;

/**
 * 限流功能接口
 *
 * @author hc
 * @date 2018-04-28 14:51
 **/
public interface RateLimiterInterface {

    /**
     * 判断是否限流
     * @param limitKey 限流Key
     * @throws OverloadException 若限流则抛出此异常
     * @throws RateLimiterException 若代码程序异常
     */
    void limit(String limitKey)
            throws OverloadException, RateLimiterException;
}
