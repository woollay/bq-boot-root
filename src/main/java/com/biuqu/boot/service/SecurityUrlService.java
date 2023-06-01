package com.biuqu.boot.service;

import org.springframework.util.MultiValueMap;

/**
 * 接口请求的安全包装服务
 * <p>
 * 经包装后可以免鉴权,用于微服务间内部调用
 *
 * @author BiuQu
 * @date 2023/3/16 07:43
 */
public interface SecurityUrlService
{
    /**
     * 安全加密原始url
     *
     * @param url 原始url
     * @return 加密后的url
     */
    String encryptUrl(String url);

    /**
     * 解密安全url
     *
     * @param url 加密的url
     * @return 原始url
     */
    String decryptUrl(String url);

    /**
     * 是否是安全的url
     * <p>
     * 适用于网关
     *
     * @param params 加密的url参数
     * @return true表示是加密url
     */
    boolean isEncUrl(MultiValueMap<String, String> params);

    /**
     * 是否是安全的url
     * <p>
     * 适用于其它服务跨服务(服务调网关转发)调用
     *
     * @param encUrl 加密的url
     * @return true表示是加密url
     */
    boolean isEncUrl(String encUrl);
}
