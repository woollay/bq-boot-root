package com.biuqu.boot.utils;

import com.biuqu.constants.Const;
import com.biuqu.utils.IoUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * Redis工具类
 *
 * @author BiuQu
 * @date 2023/2/14 08:45
 */
public final class RedisUtil
{
    /**
     * 构建redis脚本对象
     *
     * @param scriptName 脚本全路径或者脚本文件名(如果只是脚本文件时，指定从classpath:script/路径下去读)
     * @return redis脚本对象
     */
    public static DefaultRedisScript<Boolean> redisScript(String scriptName)
    {
        String scriptPath = scriptName;
        if (!scriptPath.startsWith(Const.CLASSPATH) && !scriptPath.startsWith(IoUtil.FILE_SPLIT))
        {
            scriptPath = SCRIPT_PATH + scriptPath;
        }
        return redisScript(scriptPath, Boolean.class);
    }

    /**
     * 构建redis脚本对象
     *
     * @param scriptPath 脚本全路径
     * @param clazz      返回的class类型
     * @param <T>        返回对象类型
     * @return redis脚本对象
     */
    public static <T> DefaultRedisScript<T> redisScript(String scriptPath, Class<T> clazz)
    {
        DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(scriptPath)));
        redisScript.setResultType(clazz);
        return redisScript;
    }

    private RedisUtil()
    {
    }

    /**
     * 默认的脚本路径
     */
    private static final String SCRIPT_PATH = "script/";
}
