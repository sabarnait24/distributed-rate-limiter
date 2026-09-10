local key = KEYS[1]

local limit = tonumber(ARGV[1])
local window = tonumber(ARGV[2])
local now = tonumber(ARGV[3])

local windowStart = now - window

redis.call("ZREMRANGEBYSCORE", key, 0, windowStart)

local count = redis.call("ZCARD", key)

if count >= limit then
    redis.call("EXPIRE", key, window)
    return 0
end

local member = tostring(now) .. "-" .. tostring(math.random())

redis.call("ZADD", key, now, member)

redis.call("EXPIRE", key, window)

return 1