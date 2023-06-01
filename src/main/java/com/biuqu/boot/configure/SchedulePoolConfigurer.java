package com.biuqu.boot.configure;

import com.biuqu.configure.BaseScheduleConfigurer;
import com.biuqu.model.ScheduleJob;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

/**
 * 自动加载的定时任务启动配置类
 *
 * @author BiuQu
 * @date 2023/1/27 22:40
 */
@Configuration
@EnableScheduling
public class SchedulePoolConfigurer extends BaseScheduleConfigurer
{
    @Bean
    @ConfigurationProperties(prefix = SCHEDULE_PREFIX)
    @Override
    protected List<ScheduleJob> getJobs()
    {
        List<ScheduleJob> lists = Lists.newArrayList();
        return lists;
    }

    /**
     * 周期任务的自动配置索引
     */
    private static final String SCHEDULE_PREFIX = "bq.schedule.tasks";
}
