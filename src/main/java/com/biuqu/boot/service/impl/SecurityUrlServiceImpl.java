package com.biuqu.boot.service.impl;

import com.biuqu.boot.service.SecurityUrlService;
import com.biuqu.constants.Const;
import com.biuqu.security.facade.SecurityFacade;
import com.biuqu.utils.IdUtil;
import com.biuqu.utils.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 内部url处理服务
 * <p>
 * 仅限内部调用时可以添加约定的url参数,不用鉴权
 *
 * @author BiuQu
 * @date 2023/3/15 11:41
 */
@Slf4j
@Service
public class SecurityUrlServiceImpl implements SecurityUrlService
{
    /**
     * 安全加密原始url
     *
     * @param url 原始url
     * @return 加密后的url
     */
    @Override
    public String encryptUrl(String url)
    {
        log.info("current url:{}", url);
        if (StringUtils.isEmpty(url))
        {
            return url;
        }

        try
        {
            String baseUrl = url;
            int index = url.indexOf(Const.ASK);
            if (index > 0)
            {
                baseUrl = url.substring(0, index);
            }
            List<NameValuePair> params = URLEncodedUtils.parse(new URI(url), StandardCharsets.UTF_8);
            String key = ENC_KEY + IdUtil.uuid();
            NameValuePair newPair = new BasicNameValuePair(key, this.encVal(key));
            params.add(newPair);
            baseUrl += Const.ASK + URLEncodedUtils.format(params, StandardCharsets.UTF_8);
            log.info("new enc url:{}", baseUrl);
            return baseUrl;
        }
        catch (Exception e)
        {
            log.error("failed to enc url.", e);
        }
        return url;
    }

    /**
     * 解密安全url
     *
     * @param url 加密的url
     * @return 原始url
     */
    @Override
    public String decryptUrl(String url)
    {
        log.info("current url:{}", url);
        try
        {
            if (!isEncUrl(url))
            {
                return url;
            }
            String baseUrl = url.substring(0, url.indexOf(Const.ASK));

            List<NameValuePair> params = URLEncodedUtils.parse(new URI(url), StandardCharsets.UTF_8);
            NameValuePair matchPair = getDecSource(params);
            if (null == matchPair)
            {
                log.error("invalid inner url:{}.", url);
                return url;
            }

            params.removeIf(pair -> pair.getName().equals(matchPair.getName()));
            if (CollectionUtils.isEmpty(params))
            {
                return baseUrl;
            }
            baseUrl += Const.ASK + URLEncodedUtils.format(params, StandardCharsets.UTF_8);
            log.info("new dec url:{}", baseUrl);
            return baseUrl;
        }
        catch (Exception e)
        {
            log.error("failed to dec url.", e);
        }
        return url;
    }

    /**
     * 是否是安全的url
     *
     * @param params 加密的url参数
     * @return true表示是加密url
     */
    @Override
    public boolean isEncUrl(MultiValueMap<String, String> params)
    {
        for (String key : params.keySet())
        {
            if (RegexUtil.match(key, ENC_KEY_REGEX))
            {
                String source = params.getFirst(key);
                boolean valid = this.isEncVal(key, source);
                if (valid)
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是安全的url
     *
     * @param encUrl 加密的url
     * @return true表示是加密url
     */
    @Override
    public boolean isEncUrl(String encUrl)
    {
        if (StringUtils.isEmpty(encUrl) || !encUrl.contains(Const.ASK) || !RegexUtil.match(encUrl, ENC_URL_REGEX))
        {
            return false;
        }

        try
        {
            List<NameValuePair> params = URLEncodedUtils.parse(new URI(encUrl), StandardCharsets.UTF_8);
            if (CollectionUtils.isEmpty(params))
            {
                return false;
            }
            NameValuePair matchPair = getDecSource(params);
            return null != matchPair;
        }
        catch (Exception e)
        {
            log.error("failed to dec url.", e);
        }
        return false;
    }

    /**
     * 根据原始数据生成加密的数据
     *
     * @param key 原始key
     * @return 加密后的数据
     */
    private String encVal(String key)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(key).append(Const.EQUALS);
        builder.append(System.currentTimeMillis());
        String enc = securityFacade.encrypt(builder.toString());
        log.info("current enc data:{}, before enc:{}", enc, builder);
        return enc;
    }

    /**
     * 解密数据
     *
     * @param source 加密后的数据
     * @return 解密后的数据
     */
    private String decVal(String source)
    {
        try
        {
            String dec = securityFacade.decrypt(source);
            if (StringUtils.isEmpty(dec) || !dec.contains(Const.EQUALS))
            {
                log.warn("failed to parse enc url by expression.");
                return null;
            }
            String[] sources = dec.split(Const.EQUALS);
            String encVal = sources[Const.ONE];
            if (Long.parseLong(encVal) + ENC_VALID_EXPIRE < System.currentTimeMillis())
            {
                log.warn("failed to parse enc url by expiring.");
                return null;
            }
            return dec;
        }
        catch (Exception e)
        {
            log.warn("failed to parse enc url.", e);
        }
        return null;
    }

    /**
     * 是否是安全的url
     *
     * @param key    请求key
     * @param source 加密的value
     * @return true表示是加密url对应的value
     */
    private boolean isEncVal(String key, String source)
    {
        String decValue = this.decVal(source);
        if (!StringUtils.isEmpty(decValue))
        {
            String name = decValue.split(Const.EQUALS)[0];
            return key.equalsIgnoreCase(name);
        }
        return false;
    }

    /**
     * 从url中解析出加密key对应的value
     *
     * @param params 请求地址
     * @return 只提取加密key对应的value(没有加密也没有解密)
     */
    private NameValuePair getDecSource(List<NameValuePair> params)
    {
        try
        {
            for (NameValuePair pair : params)
            {
                String name = pair.getName();
                if (RegexUtil.match(name, ENC_KEY_REGEX))
                {
                    String encSource = pair.getValue();
                    if (isEncVal(name, encSource))
                    {
                        return new BasicNameValuePair(name, StringUtils.EMPTY);
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.error("failed to enc url.", e);
        }
        return null;
    }

    /**
     * 安全key
     */
    private static final String ENC_KEY = "enc";

    /**
     * 安全key的正则表达式
     */
    private static final String ENC_KEY_REGEX = "(?<=enc)(.{" + IdUtil.uuid().length() + "})";

    /**
     * 安全key在url中的正则表达式
     */
    private static final String ENC_URL_REGEX = ENC_KEY_REGEX + "(?=\\=)";

    /**
     * 安全key的有效期
     */
    private static final long ENC_VALID_EXPIRE = TimeUnit.SECONDS.toMillis(30);

    /**
     * 本地加密服务
     */
    @Autowired
    private SecurityFacade securityFacade;
}
