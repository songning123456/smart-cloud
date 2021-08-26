package com.scframework.smartcloudgateway.loader;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Lists;
import com.scframework.smartcloudgateway.config.GatewayRouteConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author sonin
 * @date 2021/8/21 8:15
 * 动态路由加载器
 */
@Slf4j
@Component
@DependsOn({"gatewayRouteConfig"})
public class DynamicRouteLoader implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    private InMemoryRouteDefinitionRepository repository;

    private DynamicRouteService dynamicRouteService;

    private ConfigService configService;


    public DynamicRouteLoader(InMemoryRouteDefinitionRepository repository, DynamicRouteService dynamicRouteService) {
        this.repository = repository;
        this.dynamicRouteService = dynamicRouteService;
    }

    @PostConstruct
    public void init() {
        log.info("~~~初始化路由~~~");
        // 从nacos加载路由
        loadRoutesByNacos();
    }

    /**
     * 刷新路由
     */
    public void refresh() {
        this.init();
    }

    /**
     * 从nacos中读取路由配置
     */
    private void loadRoutesByNacos() {
        List<RouteDefinition> routes = Lists.newArrayList();
        configService = createConfigService();
        if (configService == null) {
            log.warn("initConfigService fail!");
        }
        try {
            String configInfo = configService.getConfig(GatewayRouteConfig.DATA_ID, GatewayRouteConfig.ROUTE_GROUP, GatewayRouteConfig.DEFAULT_TIMEOUT);
            if (StringUtils.isNotBlank(configInfo)) {
                log.info("获取网关当前配置:\r\n{}", configInfo);
                routes = JSON.parseArray(configInfo, RouteDefinition.class);
            }
        } catch (NacosException e) {
            log.error("初始化网关路由时发生错误: ", e);
            e.printStackTrace();
        }
        for (RouteDefinition definition : routes) {
            log.info("update route: {}", definition.toString());
            dynamicRouteService.add(definition);
        }
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        dynamicRouteByNacosListener(GatewayRouteConfig.DATA_ID, GatewayRouteConfig.ROUTE_GROUP);
    }

    /**
     * 监听Nacos下发的动态路由配置
     *
     * @param dataId
     * @param group
     */
    private void dynamicRouteByNacosListener(String dataId, String group) {
        try {
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("进行网关更新:\n\r{}", configInfo);
                    List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
                    for (RouteDefinition definition : definitionList) {
                        log.info("update route: {}", definition.toString());
                        dynamicRouteService.update(definition);
                    }
                }

                @Override
                public Executor getExecutor() {
                    log.info("getExecutor\n\r");
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("从nacos接收动态路由配置出错! ", e);
        }
    }

    /**
     * 创建ConfigService
     *
     * @return ConfigService
     */
    private ConfigService createConfigService() {
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", GatewayRouteConfig.SERVER_ADDR);
            properties.setProperty("namespace", GatewayRouteConfig.NAMESPACE);
            properties.setProperty("username", GatewayRouteConfig.USERNAME);
            properties.setProperty("password", GatewayRouteConfig.PASSWORD);
            return configService = NacosFactory.createConfigService(properties);
        } catch (Exception e) {
            log.error("创建ConfigService异常: ", e);
            return null;
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

}
