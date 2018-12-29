--- 计数器(滑动窗口)限流
--- 获取Java传入参数
local key = "ratelimiter:" .. KEYS[1];
local max_permits = tonumber(ARGV[1]) --- 最大限流量
local time_unit = tonumber(ARGV[2]) --- 限流数量单位时间(限流时间单位)
local window_size = tonumber(ARGV[3]) --- 滑动窗口数量(单位时间内,切分窗口数量)

--- 计算每个窗口的 : 过期时间(毫秒)
local expire = time_unit / window_size
--- 计算每个窗口的 : 限流数量
local max_per_window = max_permits / window_size

redis.log(redis.LOG_NOTICE,"max_per_window   " .. tostring(max_per_window))

--- 对每个窗口做请求计数q

local hasKey = redis.call('EXISTS', key);

if hasKey == 1 then
    local value = tonumber(redis.call('GET', key));
    if value >= max_per_window then
        return -1;
    end
end
redis.call('INCR', key);


local ttl = redis.call('TTL', key);
if ttl < 0 then
    redis.call('PEXPIRE', key, expire);
end

return 1;
