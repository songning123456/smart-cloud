package com.scframework.smartcloudgateway.nacos.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.scframework.smartcloudgateway.route.config.GatewayRouteConfig;
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
    public ConfigService createGatewayConfig() {
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", GatewayRouteConfig.SERVER_ADDR);
            properties.setProperty("namespace", GatewayRouteConfig.NAMESPACE);
            properties.setProperty("username", GatewayRouteConfig.USERNAME);
            properties.setProperty("password", GatewayRouteConfig.PASSWORD);
            return NacosFactory.createConfigService(properties);
        } catch (Exception e) {
            log.error("创建ConfigService异常: {}", e.getMessage());
            return null;
        }
    }

}
