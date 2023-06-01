package com.biuqu.boot.schedule;

import com.biuqu.boot.service.RedisService;
import com.biuqu.context.ApplicationContextHolder;
import com.biuqu.schedule.ScheduleTask;
import com.biuqu.utils.IdUtil;

import java.util.concurrent.TimeUnit;

/**
 * 抽象的独占式周期任务(多个实例上只有一个会执行)
 *
 * @author BiuQu
 * @date 2023/1/27 13:28
 */
public abstract class BaseScheduleTask implements ScheduleTask
{
    public BaseScheduleTask()
    {
        this.redis = ApplicationContextHolder.getBean(RedisService.class);
    }

    @Override
    public boolean execute(String key, long maxTime)
    {
        String execId = IdUtil.uuid();
        Boolean nxResult = redis.setNx(key, execId);
        boolean result = Boolean.TRUE.equals(nxResult);
        if (result)
        {
            if (maxTime > 0)
            {
                redis.expire(key, TimeUnit.SECONDS.toMillis(maxTime));
            }
            this.doTask(key);
        }
        return result;
    }

    @Override
    public void release(String key, long releaseDelay)
    {
        if (releaseDelay <= 0)
        {
            redis.delete(key);
        }
        else
        {
            redis.expire(key, TimeUnit.SECONDS.toMillis(releaseDelay));
        }
    }

    /**
     * redis服务
     */
    private final RedisService redis;
}
