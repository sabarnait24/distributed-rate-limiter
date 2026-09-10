local key = KEYS[1]

local capacity = tonumber(ARGV[1])
local refillRate = tonumber(ARGV[2])
local now = tonumber(ARGV[3])

local data = redis.call("HMGET", key, "tokens", "last_refill")

local tokens = tonumber(data[1])
local lastRefill = tonumber(data[2])

if tokens == nil then
    tokens = capacity
    lastRefill = now
end

local elapsed = math.max(0, now - lastRefill)

tokens = math.min(capacity, tokens + (elapsed * refillRate))

if tokens < 1 then
    redis.call("HSET", key, "tokens", tokens, "last_refill", now)
    redis.call("PEXPIRE", key, 120000)
    return 0
end

tokens = tokens - 1

redis.call("HSET", key, "tokens", tokens, "last_refill", now)
redis.call("PEXPIRE", key, 120000)

return 1