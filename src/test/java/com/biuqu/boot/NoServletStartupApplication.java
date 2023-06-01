package com.biuqu.boot;

import org.springframework.boot.SpringApplication;

/**
 * 不带Servlet容器的启动类
 * <p>
 * 需要在本类的子类中添加SpringBoot启动注解:@SpringBootApplication(exclude = {ServletStartupApplication.class})
 *
 * @author BiuQu
 * @date 2023/1/27 12:58
 */
public class NoServletStartupApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(NoServletStartupApplication.class, args);
    }
}
