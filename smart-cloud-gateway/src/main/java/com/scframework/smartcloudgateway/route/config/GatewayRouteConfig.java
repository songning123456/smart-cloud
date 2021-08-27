package com.scframework.smartcloudgateway.route.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author sonin
 * @date 2021/8/26 9:10
 * 路由配置信息
 */
@Slf4j
@Configuration
public class GatewayRouteConfig {

    public static final long DEFAULT_TIMEOUT = 30000;

    public static String SERVER_ADDR;

    public static String NAMESPACE;

    public static String USERNAME;

    public static String PASSWORD;

    public static String ROUTE_DATA_ID;

    public static String ROUTE_GROUP;

    @Value("${spring.cloud.nacos.config.server-addr}")
    public void setServerAddr(String serverAddr) {
        SERVER_ADDR = serverAddr;
    }

    @Value("${spring.cloud.nacos.config.namespace}")
    public void setNamespace(String namespace) {
        NAMESPACE = namespace;
    }

    @Value("${spring.cloud.nacos.config.username}")
    public void setUsername(String username) {
        USERNAME = username;
    }

    @Value("${spring.cloud.nacos.config.password}")
    public void setPassword(String password) {
        PASSWORD = password;
    }

    @Value("${smart.cloud.route.config.data-id:#{null}}")
    public void setRouteDataId(String dataId) {
        ROUTE_DATA_ID = dataId + ".json";
    }

    @Value("${smart.cloud.route.config.group:DEFAULT_GROUP:#{null}}")
    public void setRouteGroup(String routeGroup) {
        ROUTE_GROUP = routeGroup;
    }

}
