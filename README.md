### ratelimiter限流策略合集

---

#### Required

* Maven 3.0 + 
* JDK 1.8+
* FastJson
* Redis(分布式限流使用)

#### Features

* 支持指定Key做限流标志
* 支持多种限流策略,以及不同策略下的多元化配置
* 支持运行中动态更新限流配置,保证配置修改实时生效
* 支持分布式限流
    * 令牌桶策略
    * 计数器(固定窗口)策略
    * 计数器(滑动窗口)策略
    
#### Quickstart

* 分布式令牌桶策略(见单元测试代码DistributedTokenBucketRateLimiterTest.java)
    * 限流配置参考代码例子

```java
// 定义限流规则和配置
RateLimitRule rateLimitRule = new MapRateLimitRule();
TokenBucketLimitConfig limitConfig = new TokenBucketLimitConfig();
// 桶内最大令牌数量(单位时间内限流最大数量)
limitConfig.setMaxPermits(100);
// 限流时间单位(默认1000ms)
limitConfig.setTimeUnit(1000);
// 每次请求消耗令牌数量(默认为1)
limitConfig.setUseLimit(1);
// 添加令牌的间隔频率(单位时间内分多少次添加令牌, 为了保证令牌请求匀速, 默认分为10次)
limitConfig.setIntervalRate(10);
// 对指定请求Key("/index")做以上限流配置
rateLimitRule.addLimitConfig("/index", limitConfig);

// 定义Redis配置
RedisConfig redisConfig = new RedisConfig("127.0.0.1", 6379);

// 生成限流对象
RateLimiterInterface rateLimiterInterface = new DistributedTokenBucketRateLimiter
        .Builder()
        .rateLimitRule(rateLimitRule)
        .redisConfig(redisConfig)
        .build();

// 开始限流
try {
    rateLimiterInterface.limit("/index");
} catch (OverloadException e) {
    // 捕获OverloadException异常, 代表已经限流
} catch (RateLimiterException e) {
    // RateLimiterException异常, 代表本程序内代码异常或配置传输错误, 详见异常输出信息
}

```

* 分布式计数器(固定窗口)策略(见单元测试代码例子)
    * 限流配置参考FixWindowLimitConfig.java
    
* 分布式计数器(滑动窗口)策略(见单元测试代码例子)
    * 限流配置参考SlidingWindowLimitConfig.java

#### TODO List

* 支持单机内存的不同限流策略(P0)
* 支持限流配置通过property,yaml,json等程序配置文件自动注入(P1)
* 支持注解方式选择限流策略,并对其配置(P2)
* 支持注解方式自动应用限流到类、方法等级别(P3)




