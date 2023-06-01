package com.biuqu.boot.configure;

import com.biuqu.constants.Const;
import com.biuqu.encryptor.constants.EncryptorConst;
import com.biuqu.encryptor.model.EncryptorKey;
import com.biuqu.encryptor.model.EncryptorKeys;
import com.biuqu.security.ClientSecurity;
import com.biuqu.security.facade.SecurityFacade;
import com.biuqu.security.impl.ClientSecurityImpl;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Configuration
public class EncryptSecurityConfigurer
{
    @Bean("securityBatchKey")
    @ConfigurationProperties(prefix = "bq.encrypt.security")
    public List<EncryptorKey> securityBatchKey()
    {
        List<EncryptorKey> batchKey = new ArrayList<>(Const.TEN);
        return batchKey;
    }

    /**
     * 注入安全加密的配置秘钥信息
     *
     * @return 安全加密的配置秘钥信息
     */
    @Bean(EncryptorConst.SECURITY_KEYS)
    public EncryptorKeys securityKeys(@Qualifier("securityBatchKey") List<EncryptorKey> batchKey)
    {
        EncryptorKeys keys = new EncryptorKeys();
        keys.setKeys(batchKey);
        keys.setGm(this.gm);
        return keys;
    }

    /**
     * 注入安全加密服务门面
     *
     * @param securityKeys 安全加密的配置秘钥信息
     * @return 安全加密服务门面
     */
    @Bean(EncryptorConst.SECURITY_SERVICE)
    public SecurityFacade securityFacade(@Qualifier(EncryptorConst.SECURITY_KEYS) EncryptorKeys securityKeys)
    {
        securityKeys.setGm(gm);
        return new SecurityFacade(securityKeys);
    }

    /**
     * 客户安全服务
     *
     * @param securityFacade 本地秘钥加密器的门面
     * @return 客户安全服务
     */
    @Bean
    public ClientSecurity clientSecurity(@Qualifier(EncryptorConst.SECURITY_SERVICE) SecurityFacade securityFacade)
    {
        return new ClientSecurityImpl(securityFacade);
    }

    /**
     * 对配置文件中加密的默认类型(国密/国际加密)
     */
    @Value("${bq.encrypt.gm:true}")
    private boolean gm;
}
