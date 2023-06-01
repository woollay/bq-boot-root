package com.biuqu.boot.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.biuqu.errcode.ErrCodeEnum;
import com.biuqu.model.ResultCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 断路处理器
 *
 * @author BiuQu
 * @date 2023/3/30 16:43
 */
@Slf4j
public class CircuitBreakerHandler
{
    /**
     * 熔断降级的统一处理方法
     *
     * @param blockException 熔断降级异常
     * @return 统一的返回格式
     */
    public static ResultCode<?> handle(BlockException blockException)
    {
        log.error("sentinel circuit breaker happened.", blockException);
        return ResultCode.build(ErrCodeEnum.LIMIT_ERROR.getCode(), null);
    }
}
