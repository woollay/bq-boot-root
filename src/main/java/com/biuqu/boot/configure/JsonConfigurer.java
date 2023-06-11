package com.biuqu.boot.configure;

import com.biuqu.boot.constants.CommonBootConst;
import com.biuqu.constants.Const;
import com.biuqu.json.JsonFacade;
import com.biuqu.json.JsonIgnoreMgr;
import com.biuqu.json.JsonMaskMgr;
import com.biuqu.model.JsonIgnores;
import com.biuqu.model.JsonMask;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Json打码配置
 *
 * @author BiuQu
 * @date 2023/6/8 17:05
 */
@Configuration
public class JsonConfigurer
{
    @Bean(CommonBootConst.JSON_RULES)
    @ConfigurationProperties(prefix = "bq.json.rules")
    public List<JsonMask> jsonMasks()
    {
        return Lists.newArrayList();
    }

    @Bean(Const.JSON_MASK_SVC)
    public JsonMaskMgr maskMgr(@Qualifier(CommonBootConst.JSON_RULES) List<JsonMask> masks)
    {
        return new JsonMaskMgr(masks);
    }

    @Bean(CommonBootConst.JSON_IGNORES)
    @ConfigurationProperties(prefix = "bq.json.ignores")
    public List<JsonIgnores> jsonIgnores()
    {
        return Lists.newArrayList();
    }

    @Bean(Const.JSON_IGNORE_SVC)
    public JsonIgnoreMgr ignoreMgr(@Qualifier(CommonBootConst.JSON_IGNORES) List<JsonIgnores> ignores)
    {
        return new JsonIgnoreMgr(ignores);
    }

    @Bean
    public JsonFacade jsonFacade(@Qualifier(Const.JSON_IGNORE_SVC) JsonIgnoreMgr ignoreMgr)
    {
        return new JsonFacade(ignoreMgr);
    }
}
