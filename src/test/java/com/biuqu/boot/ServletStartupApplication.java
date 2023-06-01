package com.biuqu.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 容器启动类(默认的启动类，带servlet web容器)
 *
 * @author BiuQu
 * @date 2023/1/27 12:06
 */
public class ServletStartupApplication extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
    {
        return super.configure(builder);
    }

    public static void main(String[] args)
    {
        SpringApplication.run(ServletStartupApplication.class, args);
    }
}
