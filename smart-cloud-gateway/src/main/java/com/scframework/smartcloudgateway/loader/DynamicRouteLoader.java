package com.scframework.smartcloudgateway.loader;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Lists;
import com.scframework.smartcloudcommon.constant.CacheConstant;
import com.scframework.smartcloudcommon.redis.util.RedisUtil;
import com.scframework.smartcloudgateway.config.GatewayRouteConfig;
import com.scframework.smartcloudgateway.enums.RouteType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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

    private RedisUtil redisUtil;


    public DynamicRouteLoader(InMemoryRouteDefinitionRepository repository, DynamicRouteService dynamicRouteService, RedisUtil redisUtil) {
        this.repository = repository;
        this.dynamicRouteService = dynamicRouteService;
        this.redisUtil = redisUtil;
    }

    @PostConstruct
    public void init() {
        String dataType = GatewayRouteConfig.DATA_TYPE;
        log.info("初始化路由，dataType：" + dataType);
        // 从nacos加载路由
        if (RouteType.nacos.toString().endsWith(dataType)) {
            loadRoutesByNacos();
        }
        // 从数据库加载路由
        if (RouteType.database.toString().endsWith(dataType)) {
            loadRoutesByRedis();
        }
    }


    /**
     * 刷新路由
     *
     */
    public void refresh() {
        String dataType = GatewayRouteConfig.DATA_TYPE;
        if (!RouteType.yml.toString().endsWith(dataType)) {
            this.init();
        }
    }


    /**
     * 从nacos中读取路由配置
     */
    private void loadRoutesByNacos() {
        List<RouteDefinition> routes = Lists.newArrayList();
        configService = createConfigService();
        if (configService == null) {
            log.warn("initConfigService fail");
        }
        try {
            String configInfo = configService.getConfig(GatewayRouteConfig.DATA_ID, GatewayRouteConfig.ROUTE_GROUP, GatewayRouteConfig.DEFAULT_TIMEOUT);
            if (StringUtils.isNotBlank(configInfo)) {
                log.info("获取网关当前配置:\r\n{}", configInfo);
                routes = JSON.parseArray(configInfo, RouteDefinition.class);
            }
        } catch (NacosException e) {
            log.error("初始化网关路由时发生错误", e);
            e.printStackTrace();
        }
        for (RouteDefinition definition : routes) {
            log.info("update route : {}", definition.toString());
            dynamicRouteService.add(definition);
        }
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        dynamicRouteByNacosListener(GatewayRouteConfig.DATA_ID, GatewayRouteConfig.ROUTE_GROUP);
    }


    /**
     * 从redis中读取路由配置
     */
    private void loadRoutesByRedis() {
        List<RouteDefinition> routes = Lists.newArrayList();
        configService = createConfigService();
        if (configService == null) {
            log.warn("initConfigService fail");
        }
        Object configInfo = redisUtil.get(CacheConstant.GATEWAY_ROUTES);
        if (ObjectUtil.isNotEmpty(configInfo)) {
            log.info("获取网关当前配置:\r\n{}", configInfo);
            JSONArray array = JSON.parseArray(configInfo.toString());
            try {
                routes = getRoutesByJson(array);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                log.error("从redis中读取路由配置error: {}", e.getMessage());
            }
        }
        for (RouteDefinition definition : routes) {
            log.info("update route: {}", definition.toString());
            dynamicRouteService.add(definition);
        }
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * redis中的信息需要处理下 转成RouteDefinition对象
     * - id: login
     * uri: lb://cloud-jeecg-system
     * predicates:
     * - Path=/jeecg-boot/sys/**,
     *
     * @param array
     * @return List<RouteDefinition>
     */

    public static List<RouteDefinition> getRoutesByJson(JSONArray array) throws URISyntaxException {
        List<RouteDefinition> routeDefinitionList = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            RouteDefinition route = new RouteDefinition();
            route.setId(obj.getString("routerId"));
            Object uri = obj.get("uri");
            if (uri == null) {
                route.setUri(new URI("lb://" + obj.getString("name")));
            } else {
                route.setUri(new URI(obj.getString("uri")));
            }
            Object predicates = obj.get("predicates");
            if (predicates != null) {
                JSONArray list = JSON.parseArray(predicates.toString());
                List<PredicateDefinition> predicateDefinitionList = new ArrayList<>();
                for (Object map : list) {
                    JSONObject json = (JSONObject) map;
                    PredicateDefinition predicateDefinition = new PredicateDefinition();
                    predicateDefinition.setName(json.getString("name"));
                    JSONArray jsonArray = json.getJSONArray("args");
                    for (int j = 0; j < jsonArray.size(); j++) {
                        predicateDefinition.addArg("_genkey" + j, jsonArray.get(j).toString());
                    }
                    predicateDefinitionList.add(predicateDefinition);
                }
                route.setPredicates(predicateDefinitionList);
            }

            Object filters = obj.get("filters");
            if (filters != null) {
                JSONArray list = JSON.parseArray(filters.toString());
                List<FilterDefinition> filterDefinitionList = new ArrayList<>();
                if (ObjectUtil.isNotEmpty(list)) {
                    for (Object map : list) {
                        JSONObject json = (JSONObject) map;
                        JSONArray jsonArray = json.getJSONArray("args");
                        String name = json.getString("name");
                        FilterDefinition filterDefinition = new FilterDefinition();
                        for (Object o : jsonArray) {
                            JSONObject params = (JSONObject) o;
                            filterDefinition.addArg(params.getString("key"), params.get("value").toString());
                        }
                        filterDefinition.setName(name);
                        filterDefinitionList.add(filterDefinition);
                    }
                    route.setFilters(filterDefinitionList);
                }
            }
            routeDefinitionList.add(route);
        }
        return routeDefinitionList;
    }

    /**
     * 监听Nacos下发的动态路由配置
     *
     * @param dataId
     * @param group
     */
    public void dynamicRouteByNacosListener(String dataId, String group) {
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
            log.error("从nacos接收动态路由配置出错!", e);
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
            log.error("创建ConfigService异常", e);
            return null;
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

}
