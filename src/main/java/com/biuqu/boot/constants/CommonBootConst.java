package com.biuqu.boot.constants;

/**
 * 微服务常量
 *
 * @author BiuQu
 * @date 2023/2/1 09:02
 */
public final class CommonBootConst
{
    /**
     * Http参数
     */
    public static final String HTTP_PARAMS = "httpParams";

    /**
     * Json规则参数
     */
    public static final String JSON_RULES = "jsonRules";

    /**
     * Json忽略参数
     */
    public static final String JSON_IGNORES = "jsonIgnores";

    /**
     * jwtChannel配置
     */
    public static final String JWT_CHANNEL_CONFIG = "jwtChannel";

    /**
     * 最大调用量的redis脚本对象名
     */
    public static final String MAX_REDIS_SCRIPT_SVC = "maxRestScriptSvc";

    /**
     * qps的redis脚本对象名
     */
    public static final String QPS_REDIS_SCRIPT_SVC = "qpsRestScriptSvc";

    /**
     * trace id
     */
    public static final String TRACE_ID = "traceId";

    /**
     * span id
     */
    public static final String SPAN_ID = "spanId";

    /**
     * trace id的标记
     */
    public static final char TRACE_TAG = 'M';

    private CommonBootConst()
    {
    }
}
