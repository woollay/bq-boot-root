package com.biuqu.boot.configure;

import com.biuqu.boot.constants.CommonBootConst;
import com.biuqu.constants.Const;
import com.biuqu.http.*;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Http请求的配置器(统一使用Apache HttpComponents封装)：
 * <p>
 * 1.使用Apache HttpComponents覆写Spring自带的RestTemplate的底层实现
 * 2.使用Apache HttpComponents实现一个超级简单的HttpClient
 *
 * @author BiuQu
 * @date 2023/1/27 22:50
 */
@Configuration
public class HttpConfigurer
{
    @Bean
    @ConfigurationProperties(prefix = "bq.http.rest")
    public HttpParam restParam()
    {
        return new HttpParam();
    }

    @Bean(CommonBootConst.HTTP_PARAMS)
    @ConfigurationProperties(prefix = "bq.http.clients")
    public List<HttpParam> httpParams()
    {
        return Lists.newArrayList();
    }

    /**
     * 支持负载均衡
     *
     * @param param 配置参数
     * @return 封装后的RestTemplate
     */
    @Bean
    @LoadBalanced
    public CommonRestTemplate restTemplate(HttpParam param)
    {
        HttpClientMgr clientMgr = new HttpClientMgr(param);
        CommonRestTemplate restTemplate = new RestTemplateFactory(clientMgr).create();
        //设置请求的编码
        List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
        httpMessageConverters.stream().forEach(httpMessageConverter ->
        {
            if (httpMessageConverter instanceof StringHttpMessageConverter)
            {
                StringHttpMessageConverter messageConverter = (StringHttpMessageConverter)httpMessageConverter;
                //设置编码为UTF-8
                messageConverter.setDefaultCharset(StandardCharsets.UTF_8);
            }
        });
        return restTemplate;
    }

    @Primary
    @Bean("defaultHttpClient")
    public CommonHttpClient defaultHttpClient()
    {
        List<HttpParam> params = this.httpParams();
        HttpParam param;
        if (params.size() <= 0)
        {
            param = new HttpParam();
        }
        else
        {
            param = params.get(0);
        }
        HttpClientMgr clientMgr = new HttpClientMgr(param);
        return new CommonHttpClient(clientMgr);
    }

    @Bean("initHttpHttpClient")
    public CommonHttpClient initHttpClient()
    {
        List<HttpParam> params = this.httpParams();
        HttpParam param;
        if (params.size() > Const.ONE)
        {
            param = params.get(Const.ONE);
        }
        else
        {
            param = new HttpParam();
        }
        HttpClientMgr clientMgr = new HttpClientMgr(param);
        return new CommonHttpClient(clientMgr);
    }
}
