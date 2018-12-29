--- 获取Java传入参数
local key = "ratelimiter:" .. KEYS[1] --- 限流KEY
local use_limit = tonumber(ARGV[1]) --- 请求令牌数量
local curr_millis = ARGV[2] --- 当前请求时间戳(毫秒)
local max_permits = tonumber(ARGV[3]) --- 单位时间内的最大限流量(最大令牌数量)
local interval_rate = tonumber(ARGV[4]) --- 添加令牌的间隔次数(每单位时间内分成多少次添加令牌)
-- local time_unit = tonumber(ARGV[5]) --- 限流的时间范围(多少秒内允许max_permits次请求)(单位秒)

--- 获取之前存储记录
local rate_limit_info = redis.pcall("HMGET", key, "last_mill_second", "curr_permits", "max_permits", "interval_millis", "interval_permits")
local last_mill_second = rate_limit_info[1] --- 上一次更新令牌时间
local curr_permits = tonumber(rate_limit_info[2]) --- 当前桶内令牌数量

--- 计算临时变量
local interval_millis = 1000 / interval_rate --- 添加令牌的间隔频率(单位毫秒)
local interval_permits = math.floor(max_permits / interval_rate) --- 间隔时间内,需要添加的令牌数量
local local_curr_permits; --- 当前令牌数量
redis.pcall("HSET", key, "max_permits", max_permits)
redis.pcall("HSET", key, "interval_millis", interval_millis)
redis.pcall("HSET", key, "interval_permits", interval_permits)

--- 令牌桶已存在
if (type(last_mill_second) ~= 'boolean' and last_mill_second ~= false and last_mill_second ~= nil) then

    --- 根据时间间隔,计算本次请求的时间段, 若在一个时间段内, 则不添加令牌. 若不在一个时间段内, 则添加令牌. -> 保证令牌桶的令牌流入速度是均匀的
    --- reverse_permits,大于0表示不在一个时间段内, 则根据时间差值, 放入倍数令牌数量reverse_permits
    local reverse_permits = math.floor(((curr_millis - last_mill_second) / interval_millis) * interval_permits)
    if (reverse_permits < 0) then
        reverse_permits = 0
    end
    if (reverse_permits > 0) then
        redis.pcall("HSET", key, "last_mill_second", curr_millis)
    end

    --- 向桶里添加令牌,若超过最大令牌数量,则丢弃
    local expect_curr_permits = reverse_permits + curr_permits;
    local_curr_permits = math.min(expect_curr_permits, max_permits);
else
    --- 第一次初始化时, 防止冷启动, 把桶直接填满.更新当前时间为最后更新时间
    local_curr_permits = max_permits
    redis.pcall("HSET", key, "last_mill_second", curr_millis)
end

--- 获取令牌结果判断
local result = -1
if (local_curr_permits - use_limit >= 0) then
    result = 1
    redis.pcall("HSET", key, "curr_permits", local_curr_permits - use_limit)
else
    redis.pcall("HSET", key, "curr_permits", local_curr_permits)
end
return result