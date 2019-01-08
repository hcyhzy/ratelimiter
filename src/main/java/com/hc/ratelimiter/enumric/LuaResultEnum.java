package com.hc.ratelimiter.enumric;

/**
 * Lua 返回结果
 *
 * @author hc
 **/
public enum LuaResultEnum {

    /**
     * lua : 成功
     */
    SUCCESS(1L),
    /**
     * lua : 失败
     */
    FAIL(-1L);

    private Long result;

    LuaResultEnum(Long result) {
        this.result = result;
    }

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }
}
