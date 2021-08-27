package com.scframework.smartcloudgateway.route.publish;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Lists;
import com.scframework.smartcloudgateway.nacos.config.NacosConfig;
import com.scframework.smartcloudgateway.nacos.listener.GatewayRouteListener;
import com.scframework.smartcloudgateway.route.config.GatewayRouteConfig;
import com.scframework.smartcloudgateway.route.service.IDynamicRouteCrud;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author sonin
 * @date 2021/8/21 8:15
 * 动态路由加载器
 */
@Slf4j
@Component
@DependsOn({"gatewayRouteConfig"})
public class DynamicRoutePublish implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Autowired
    private IDynamicRouteCrud iDynamicRouteCrud;

    @Autowired
    private GatewayRouteListener gatewayRouteListener;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @PostConstruct
    public void init() {
        log.info("~~~初始化路由~~~");
        // 从nacos加载路由
        loadRoutesByNacos();
    }

    /**
     * 从nacos中读取路由配置
     */
    private void loadRoutesByNacos() {
        List<RouteDefinition> routes = Lists.newArrayList();
        NacosConfig nacosConfig = new NacosConfig();
        ConfigService configService = nacosConfig.createGatewayConfig();
        if (configService == null) {
            log.warn("init gateway config fail!");
        }
        try {
            assert configService != null;
            String configInfo = configService.getConfig(GatewayRouteConfig.ROUTE_DATA_ID, GatewayRouteConfig.ROUTE_GROUP, GatewayRouteConfig.DEFAULT_TIMEOUT);
            if (StringUtils.isNotBlank(configInfo)) {
                log.info("获取网关当前配置:\r\n{}", configInfo);
                routes = JSON.parseArray(configInfo, RouteDefinition.class);
            }
        } catch (NacosException e) {
            log.error("初始化网关路由时发生错误: {}", e.getMessage());
        }
        for (RouteDefinition definition : routes) {
            log.info("update route: {}", definition.toString());
            iDynamicRouteCrud.add(definition);
        }
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        // 监听nacos网关路由变化
        try {
            configService.addListener(GatewayRouteConfig.ROUTE_DATA_ID, GatewayRouteConfig.ROUTE_GROUP, gatewayRouteListener);
        } catch (Exception e) {
            log.error("从nacos接收动态路由配置出错: {}", e.getMessage());
        }
    }

}
