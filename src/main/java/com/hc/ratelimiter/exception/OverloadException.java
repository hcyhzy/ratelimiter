package com.hc.ratelimiter.exception;

/**
 * 限流过载异常
 * @author hc
 * @date 2018-04-28 14:51
 **/
public class OverloadException extends RuntimeException {

    public OverloadException() {
        super();
    }

    public OverloadException(String message) {
        super(message);
    }

    public OverloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverloadException(Throwable cause) {
        super(cause);
    }

    protected OverloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
