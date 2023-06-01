package com.biuqu.boot.service;

import com.biuqu.constants.Const;
import com.biuqu.utils.IdUtil;
import com.biuqu.utils.RegexUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SecurityUrlServiceTest
{

    @Test
    public void encryptUrl() throws URISyntaxException
    {
        String url = "/test/abc/asd?xxx=yyy&enc" + IdUtil.uuid() + "=abc";
        String baseUrl = url;
        int index = url.indexOf(Const.ASK);
        if (index > 0)
        {
            baseUrl = url.substring(0, index);
        }

        List<NameValuePair> params = URLEncodedUtils.parse(new URI(url), StandardCharsets.UTF_8);

        String key = "enc" + IdUtil.uuid();
        NameValuePair newPair = new BasicNameValuePair(key, "abc");
        params.add(newPair);

        baseUrl += Const.ASK + URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        System.out.println("current url=" + url + ",new url=" + baseUrl);

        List<NameValuePair> params2 = URLEncodedUtils.parse(new URI(baseUrl), StandardCharsets.UTF_8);
        params2.removeIf(pair -> pair.getName().equals(key));
        int index2 = baseUrl.indexOf(Const.ASK);
        String newUrl = baseUrl;
        if (index > 0)
        {
            newUrl = baseUrl.substring(0, index2);
        }
        newUrl += Const.ASK + URLEncodedUtils.format(params2, StandardCharsets.UTF_8);

        System.out.println("url1=" + url + "\r\nurl2=" + baseUrl + "\r\nurl3=" + newUrl);
    }

    @Test
    public void decryptUrl()
    {
    }

    @Test
    public void isEncUrl()
    {
        int len = IdUtil.uuid().length();
        //非贪婪匹配'(.*?)'中的'?'
        final String REGEX = "(?<=enc)(.{" + len + "})(?=\\=)";
        System.out.println("encRegex=" + REGEX);

        String encRegex = "enc.*\\{" + IdUtil.uuid().length() + "\\}\\=";
        System.out.println("encRegex=" + encRegex);
        String url = "/test/abc/asd?enc" + IdUtil.uuid() + "=abc";
        System.out.println("regex match=" + RegexUtil.match(url, REGEX));

        String url2 = "/test/abc/asd?enc" + IdUtil.uuid() + "2=abc";
        System.out.println("regex2 match=" + RegexUtil.match(url2, REGEX));

        final String REGEX2 = "(?<=enc)(.{" + len + "})";
        String url3 = "/test/abc/asd?enc" + IdUtil.uuid() + "2";
        System.out.println("regex3 match=" + RegexUtil.match(url3, REGEX2));
    }
}