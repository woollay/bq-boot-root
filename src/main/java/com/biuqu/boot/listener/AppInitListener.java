package com.biuqu.boot.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * 应用启动监听
 *
 * @author BiuQu
 * @date 2023/1/27 13:08
 */
@Slf4j
public class AppInitListener implements ApplicationListener<ApplicationReadyEvent>
{
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event)
    {
        init();
    }

    /**
     * 初始化
     * <p>
     * 应用场景：
     * 1.当应用启动完成后，把校验框架配置的错误码替换成对应的msg(可做2次映射：错误码国文件中配置标准错误码，在参数错误时，还可以再映射1次);
     * 2.新增启动定时任务，做缓存自动刷新
     */
    protected void init()
    {
        log.info("bq application ready now.");
    }
}
