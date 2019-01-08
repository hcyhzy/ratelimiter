package com.hc.ratelimiter.exception;

/**
 * 程序异常
 * @author hc
 **/
public class RateLimiterException extends RuntimeException {

    public RateLimiterException() {
        super();
    }

    public RateLimiterException(String message) {
        super(message);
    }

    public RateLimiterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RateLimiterException(Throwable cause) {
        super(cause);
    }

    protected RateLimiterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
