package com.biuqu.boot.configure;

import com.biuqu.boot.constants.CommonBootConst;
import com.biuqu.boot.utils.RedisUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis注入配置
 *
 * @author BiuQu
 * @date 2023/1/27 13:31
 */
@Configuration
public class RedisConfigurer
{
    @Primary
    @Bean
    public RedisTemplate<String, Object> instance(RedisConnectionFactory factory)
    {
        RedisTemplate<String, Object> redis = new RedisTemplate<>();
        redis.setConnectionFactory(factory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        redis.setKeySerializer(keySerializer);
        redis.setHashKeySerializer(keySerializer);

        Jackson2JsonRedisSerializer<?> valueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        valueSerializer.setObjectMapper(mapper);

        redis.setValueSerializer(valueSerializer);
        redis.setHashValueSerializer(valueSerializer);

        redis.afterPropertiesSet();

        return redis;
    }

    @Autowired
    public void redisProperties(RedisProperties redisProperties)
    {
        //TODO 支持redis密码托管场景重新设置密码
    }

    @Bean(CommonBootConst.MAX_REDIS_SCRIPT_SVC)
    public DefaultRedisScript<Boolean> maxRedisScript()
    {
        return RedisUtil.redisScript(MAX_SCRIPT);
    }

    @Bean(CommonBootConst.QPS_REDIS_SCRIPT_SVC)
    public DefaultRedisScript<Boolean> qpsRedisScript()
    {
        return RedisUtil.redisScript(QPS_SCRIPT);
    }

    /**
     * qps限流脚本
     */
    private static final String QPS_SCRIPT = "access_limit.lua";

    /**
     * 最大调用量限流脚本
     */
    private static final String MAX_SCRIPT = "max_limit.lua";
}
