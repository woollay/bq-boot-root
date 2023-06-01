package com.biuqu.boot.configure;

import com.biuqu.thread.CommonThreadPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置(spring 池化管理，可以自动注入链路跟踪)
 * <p>
 * 定义了2个线程池(默认使用jdk创建的,额外定义了1个由spring创建的池)
 *
 * @author BiuQu
 * @date 2023/2/26 12:14
 */
@Configuration
@EnableAsync
public class ThreadPoolConfigurer implements AsyncConfigurer
{
    @Override
    public Executor getAsyncExecutor()
    {
        return CommonThreadPool.getExecutor("asyncPool", CORE_NUM, MAX_NUM);
    }

    @Bean(value = "customAsync", destroyMethod = "shutdown")
    public static Executor customPool()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(CORE_NUM);
        //配置最大线程数sss
        executor.setMaxPoolSize(MAX_NUM);
        //配置队列大小
        executor.setQueueCapacity(DEFAULT_SIZE);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("customAsyncPool");
        //配置保存时间
        executor.setKeepAliveSeconds(DEFAULT_KEEP_ALIVE);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

    /**
     * 默认线程空闲后存活时间
     */
    private static final int DEFAULT_KEEP_ALIVE = 60;

    /**
     * 核心线程数
     */
    private static final int CORE_NUM = 10;

    /**
     * 最大线程数
     */
    private static final int MAX_NUM = 20;

    /**
     * 默认队列大小
     */
    private static final int DEFAULT_SIZE = 500;
}
