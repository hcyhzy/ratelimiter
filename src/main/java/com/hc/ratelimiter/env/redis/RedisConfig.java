package com.hc.ratelimiter.env.redis;

/**
 * Redis配置
 *
 * @author hc
 * @date 2018-04-28 15:35
 **/
public class RedisConfig {

    /**
     * redis默认端口
     */
    private static final int DEFAULT_PORT = 6379;

    /**
     * redis连接超时时间 : 默认200ms
     */
    private static final int DEFAULT_CONNECT_TIME_OUT = 200;

    /**
     * host
     */
    private String host;

    /**
     * port
     */
    private int port = DEFAULT_PORT;

    /**
     * 密码
     */
    private String password;

    /**
     * 连接超时时间
     */
    private int connectTimeOut = DEFAULT_CONNECT_TIME_OUT;


    /**
     * 连接池配置
     */
    private RedisPoolConfig redisPoolConfig = new RedisPoolConfig.Builder().build();

    public RedisConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public RedisConfig(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public RedisConfig(String host, int port, String password, RedisPoolConfig redisPoolConfig) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.redisPoolConfig = redisPoolConfig;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public RedisPoolConfig getRedisPoolConfig() {
        return redisPoolConfig;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }
}
