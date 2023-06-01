-- 获取限流key
local limitKey = KEYS[1]
--redis.log(redis.LOG_WARNING,'access limit key is:',limitKey)

-- 调用脚本传入的限流大小
local limitNum = tonumber(ARGV[1])
-- 传入数据的有效期(ms,比如1秒)
local expireMills = tonumber(ARGV[2])
-- 传入当前时间(ms)
local nowMills = tonumber(ARGV[3])

--清除过期数据
local expiredTimeMills = nowMills-expireMills;
redis.call('zremrangebyscore',limitKey,0,expiredTimeMills);

local count = redis.call('zcard',limitKey);

-- 获取当前流量大小
local countNum = tonumber(count or "0")
--redis.log(redis.LOG_WARNING,'access countNum=',countNum)

--是否超出限流值
if countNum + 1 > limitNum then
    -- 拒绝访问
--    redis.log(redis.LOG_WARNING,'***access beyond limit max:',limitNum)
    return true
else
    -- 没有超过阈值,设置当前访问数量+1
    redis.call('zadd',limitKey,nowMills,nowMills)
    -- 设置过期时间(ms,相当于给这个zset key自动续期)
    redis.call('pexpire',limitKey,expireMills)
    -- 放行
--    redis.log(redis.LOG_WARNING,'***access smaller than limit max:',limitNum)
    return false
end
