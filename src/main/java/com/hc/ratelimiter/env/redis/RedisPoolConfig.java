package com.hc.ratelimiter.env.redis;

/**
 * Redis池化配置
 *
 * @author hc
 * @date 2018-04-28 15:39
 **/
public class RedisPoolConfig {

    /**
     * 最大空闲
     */
    private int maxIdle;

    /**
     * 最大线程
     */
    private int maxTotal;

    /**
     * 最小空闲
     */
    private int minIdle;

    /**
     * 最大等待时间
     */
    private int maxWaitMillis;

    /**
     * 是否检查连接池可用性
     */
    private boolean testOnBorrow;

    public int getMaxIdle() {
        return maxIdle;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    private RedisPoolConfig(Builder builder) {
        maxIdle = builder.maxIdle;
        maxTotal = builder.maxTotal;
        minIdle = builder.minIdle;
        maxWaitMillis = builder.maxWaitMillis;
        testOnBorrow = builder.testOnBorrow;
    }

    public static final class Builder {

        private int maxIdle = 100;

        private int maxTotal = 100;

        private int minIdle = 10;

        private int maxWaitMillis = 10;

        private boolean testOnBorrow = true;

        public Builder() {
        }

        public Builder maxIdle(int val) {
            maxIdle = val;
            return this;
        }

        public Builder maxTotal(int val) {
            maxTotal = val;
            return this;
        }

        public Builder minIdle(int val) {
            minIdle = val;
            return this;
        }

        public Builder maxWaitMillis(int val) {
            maxWaitMillis = val;
            return this;
        }

        public Builder testOnBorrow(boolean val) {
            testOnBorrow = val;
            return this;
        }

        public RedisPoolConfig build() {
            return new RedisPoolConfig(this);
        }
    }
}
