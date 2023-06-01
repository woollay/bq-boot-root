package com.biuqu.boot.configure;

import com.biuqu.constants.Const;
import com.biuqu.encryptor.constants.EncryptorConst;
import com.biuqu.encryptor.model.EncryptorKey;
import com.biuqu.encryptor.model.EncryptorKeys;
import com.biuqu.hsm.BizHsmFacade;
import com.biuqu.hsm.facade.HsmFacade;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全配置服务
 * <p>
 * 1.包括模拟加密机和本地秘钥的安全加密服务；
 * 2.加密机用于处理内循环数据，如文件、数据库等的存取数据；
 * 3.安全加密服务用于与外部对接，做加解密认证；
 *
 * @author BiuQu
 * @date 2023/5/9 07:59
 */
@Configuration
public class EncryptHsmConfigurer
{
    @Bean("hsmBatchKey")
    @ConfigurationProperties(prefix = "bq.encrypt.hsm")
    public List<EncryptorKey> hsmBatchKey()
    {
        List<EncryptorKey> batchKey = new ArrayList<>(Const.TEN);
        return batchKey;
    }

    /**
     * 注入加密机的配置秘钥信息
     *
     * @return 加密机的配置秘钥信息
     */
    @Bean(EncryptorConst.HSM_KEYS)
    public EncryptorKeys hsmKeys(@Qualifier("hsmBatchKey") List<EncryptorKey> batchKey)
    {
        EncryptorKeys keys = new EncryptorKeys();
        keys.setKeys(batchKey);
        keys.setGm(this.gm);
        return keys;
    }

    /**
     * 注入加密机服务门面
     *
     * @param hsmKeys 加密机的配置秘钥信息
     * @return 加密机服务门面
     */
    @Bean(EncryptorConst.HSM_SERVICE)
    public HsmFacade hsmFacade(@Qualifier(EncryptorConst.HSM_KEYS) EncryptorKeys hsmKeys)
    {
        return new HsmFacade(hsmKeys);
    }

    /**
     * 注入业务安全服务
     *
     * @param hsmFacade 加密机服务
     * @return 业务安全服务
     */
    @Bean
    public BizHsmFacade hsmBizFacade(@Qualifier(EncryptorConst.HSM_SERVICE) HsmFacade hsmFacade)
    {
        return new BizHsmFacade(hsmFacade);
    }

    /**
     * 对配置文件中加密的默认类型(国密/国际加密)
     */
    @Value("${bq.encrypt.gm:true}")
    private boolean gm;
}
