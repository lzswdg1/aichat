--KETS[1]:限流的key，比如user:123
--ARGV[1]:限流的时间窗口（秒）
--ARGV[2]:最大请求数

local key =KEYS[1]
local window = tonumber(ARGV[1])
local limit = tonumber(ARGV[2])

--当前请求数
local currunt = tonumber(redis.call("GET",key) or "0")

if  currunt +1 > limit then
    return 0
else
    currunt = redis.call("INCR",key)
    if currunt == 1 then
        redis.call("EXPIRE",key,window)
    end
    return 1
end