package com.hc.ratelimiter.enumric;

/**
 * 限流类型
 *
 * @author hc
 **/
public enum LimitTypeEnum {

    /**
     * 分布式令牌桶
     */
    DISTRIBUTED_TOKEN_BUCKET;

    LimitTypeEnum() {
    }
}
