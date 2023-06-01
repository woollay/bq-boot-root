package com.biuqu.boot.utils;

import com.biuqu.model.ResultCode;
import com.biuqu.utils.JsonUtil;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * 响应工具类
 *
 * @author BiuQu
 * @date 2023/2/28 23:26
 */
public final class ResponseUtil
{
    /**
     * 回写标准的异常结果
     *
     * @param response servlet响应对象
     * @param code     错误码
     * @param snake    是否转驼峰
     * @throws IOException 网络异常
     */
    public static void writeErrorBody(HttpServletResponse response, String code, boolean snake) throws IOException
    {
        ResultCode<?> resultCode = ResultCode.error(code);
        String json = JsonUtil.toJson(resultCode, snake);
        writeErrorBody(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, json);
    }

    /**
     * 回写标准的异常结果
     *
     * @param response servlet响应对象
     * @param json     body报文
     * @throws IOException 网络异常
     */
    public static void writeErrorBody(HttpServletResponse response, String json) throws IOException
    {
        writeErrorBody(response, HttpStatus.SC_OK, json);
    }

    /**
     * 回写标准的异常结果
     *
     * @param response servlet响应对象
     * @param httpCode http错误码
     * @param json     body报文
     * @throws IOException 网络异常
     */
    public static void writeErrorBody(HttpServletResponse response, Integer httpCode, String json) throws IOException
    {
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        response.setStatus(httpCode);
        Writer writer = response.getWriter();
        writer.write(json);
        writer.flush();
    }

    private ResponseUtil()
    {
    }
}
