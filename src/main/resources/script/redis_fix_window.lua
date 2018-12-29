--- 计数器(固定窗口)限流
--- 获取Java传入参数
local key = "ratelimiter:" .. KEYS[1];
local max_permits = tonumber(ARGV[1]) --- 最大限流量
local time_unit = tonumber(ARGV[2]) --- 过期时间毫秒(限流时间单位)

local hasKey = redis.call('EXISTS', key);

if hasKey == 1 then
    local value = tonumber(redis.call('GET', key));
    if value >= max_permits then
        return -1;
    end
end
redis.call('INCR', key);


local ttl = redis.call('TTL', key);
if ttl < 0 then
    redis.call('PEXPIRE', key, time_unit);
end

return 1;
