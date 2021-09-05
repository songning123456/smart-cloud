package com.scframework.smartcloudoauth2.nacos.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @author sonin
 * @date 2021/8/26 9:42
 */
@Slf4j
public class NacosConfig {

    /**
     * 创建gateway config
     *
     * @return ConfigService
     */
    public ConfigService createOauth2Config() {
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", Oauth2Config.SERVER_ADDR);
            properties.setProperty("username", Oauth2Config.USERNAME);
            properties.setProperty("password", Oauth2Config.PASSWORD);
            return NacosFactory.createConfigService(properties);
        } catch (Exception e) {
            log.error("创建ConfigService异常: {}", e.getMessage());
            return null;
        }
    }

}
