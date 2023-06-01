package com.biuqu.boot.service.impl;

import com.biuqu.boot.configure.RedisConfigurer;
import com.biuqu.boot.service.RedisService;
import com.biuqu.context.ApplicationContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis封装并简化使用的实现类
 *
 * @author BiuQu
 * @date 2023/1/27 17:15
 */
@Service
public class RedisServiceImpl implements RedisService
{
    @Override
    public Boolean expire(String key, long expireAt)
    {
        return redis.expire(key, expireAt, TimeUnit.MILLISECONDS);
    }

    @Override
    public void delete(String key)
    {
        redis.delete(key);
    }

    @Override
    public <T> T execute(String script, Class<T> clazz, List<String> keys, Object... param)
    {
        if (StringUtils.isEmpty(script))
        {
            return null;
        }

        DefaultRedisScript<T> redisScript = ApplicationContextHolder.getBean(script);
        return redis.execute(redisScript, keys, param);
    }

    @Override
    public <T> T get(String key)
    {
        return (T)redis.opsForValue().get(key);
    }

    @Override
    public <T> void set(String key, T value)
    {
        redis.opsForValue().set(key, value);
    }

    @Override
    public <T> Boolean setNx(String key, T value)
    {
        return redis.opsForValue().setIfAbsent(key, value);
    }

    @Override
    public <T> T hGet(String key, Object field)
    {
        return (T)redis.opsForHash().get(key, field);
    }

    @Override
    public <T> void hSet(String key, Object field, T value)
    {
        redis.opsForHash().put(key, field, value);
    }

    @Override
    public <T> Long sAdd(String key, T... values)
    {
        return redis.opsForSet().add(key, values);
    }

    @Override
    public <T> Long sRem(String key, T... values)
    {
        return redis.opsForSet().remove(key, values);
    }

    @Override
    public <T> Set<T> sMembers(String key)
    {
        return (Set<T>)redis.opsForSet().members(key);
    }

    @Override
    public <T> Boolean zAdd(String key, T value, long score)
    {
        return redis.opsForZSet().add(key, value, score);
    }

    @Override
    public <T> Long zRem(String key, T... values)
    {
        return redis.opsForZSet().remove(key, values);
    }

    @Override
    public <T> Set<T> zRangeByScore(String key, long minScore, long maxScore)
    {
        return (Set<T>)redis.opsForZSet().rangeByScore(key, minScore, maxScore);
    }

    /**
     * 注入redis底层实现
     * ({@link RedisConfigurer#instance(RedisConnectionFactory)})
     */
    @Autowired
    private RedisTemplate<String, Object> redis;
}
