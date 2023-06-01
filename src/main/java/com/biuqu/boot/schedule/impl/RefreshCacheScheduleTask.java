package com.biuqu.boot.schedule.impl;

import com.biuqu.boot.service.RedisService;
import com.biuqu.cache.CacheFactory;
import com.biuqu.schedule.ScheduleTask;
import com.biuqu.utils.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 全局缓存刷新周期任务(每个实例上都要执行)
 *
 * @author BiuQu
 * @date 2023/1/27 13:25
 */
@Component("globalCacheRefreshTask")
public class RefreshCacheScheduleTask implements ScheduleTask
{
    @Override
    public boolean execute(String key, long maxTime)
    {
        return false;
    }

    @Override
    public void release(String key, long releaseDelay)
    {
    }

    @Override
    public void doTask(String key)
    {
        //1.获取最新的缓存value
        String value = IdUtil.uuid();
        Boolean nxResult = redis.setNx(key, value);
        if (Boolean.FALSE.equals(nxResult))
        {
            value = redis.get(key);
        }

        //2.如果和上次的一致，则说明缓存已经刷新过了，否则需要刷新缓存
        if (!value.equalsIgnoreCase(lastId))
        {
            LOGGER.info("Now clearing all cached data by key:{}", key);
            CacheFactory.invalidateAll();
            this.lastId = value;
        }
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshCacheScheduleTask.class);

    /**
     * redis服务
     */
    @Autowired
    private RedisService redis;

    /**
     * 执行id
     */
    private String lastId;
}
