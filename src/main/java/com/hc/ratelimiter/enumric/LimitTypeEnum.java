package com.hc.ratelimiter.enumric;

/**
 * 限流类型
 *
 * @author hc
 * @date 2018-04-28 11:47
 **/
public enum LimitTypeEnum {

    /**
     * 分布式令牌桶
     */
    DISTRIBUTED_TOKEN_BUCKET;

    LimitTypeEnum() {
    }
}
