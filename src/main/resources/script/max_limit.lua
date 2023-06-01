-- 获取限流key
local limitKey = KEYS[1]
--redis.log(redis.LOG_WARNING,'max limit key is:',limitKey)

-- 调用脚本传入的限流大小
local limitNum = tonumber(ARGV[1])
--redis.log(redis.LOG_WARNING,'max limit num is:',limitNum)

-- 传入过期时间(ms)
local expireMills = tonumber(ARGV[2])
--redis.log(redis.LOG_WARNING,'max limit expire is:',expireMills)

local count = redis.call('get',limitKey);
--redis.log(redis.LOG_WARNING,'max limit count is:',count)

if count then
    -- 获取当前流量大小
    local countNum = tonumber(count or "0")
--    redis.log(redis.LOG_WARNING,'max countNum=',countNum)
    --是否超出限流值
    if countNum + 1 > limitNum then
        -- 拒绝访问
--        redis.log(redis.LOG_WARNING,'***max beyond limit max:',limitNum)
        return true
    else
        -- 没有超过阈值,设置当前访问数量+1
        redis.call('incrby',limitKey,1)
        -- 放行
--        redis.log(redis.LOG_WARNING,'***max smaller than limit max:',limitNum)
        return false
    end
else
    -- 没有超过阈值,设置当前访问数量+1
    redis.call('set',limitKey,1)
    -- 设置过期时间(ms)
    redis.call('pexpire',limitKey,expireMills)
    -- 放行
--    redis.log(redis.LOG_WARNING,'***max smaller than limit max:',1)
    return false
end

