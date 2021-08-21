package com.scframework.smartcloudgateway.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author sonin
 * @date 2021/8/21 10:43
 * 聚合各个服务的swagger接口
 */
@Component
@Slf4j
public class CloudSwaggerResourceProvider implements SwaggerResourcesProvider {

    /**
     * swagger2默认的url后缀
     */
    private static final String SWAGGER2URL = "/v2/api-docs";

    /**
     * 网关路由
     */
    private final RouteLocator routeLocator;

    /**
     * 网关应用名称
     */
    @Value("${spring.application.name}")
    private String self;

    @Autowired
    public CloudSwaggerResourceProvider(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routeHosts = new ArrayList<>();
        // 获取所有可用的host:serviceId
        routeLocator.getRoutes().filter(route -> route.getUri().getHost() != null).filter(route -> !self.equals(route.getUri().getHost())).subscribe(route -> routeHosts.add(route.getUri().getHost()));
        // 记录已经添加过的server，存在同一个应用注册了多个服务在eureka上
        Set<String> dealSet = new HashSet<>();
        routeHosts.forEach(instance -> {
            // 拼接url
            String url = "/" + instance.toLowerCase() + SWAGGER2URL;
            if (!dealSet.contains(url)) {
                dealSet.add(url);
                log.info("Gateway add SwaggerResource: {}", url);
                SwaggerResource swaggerResource = new SwaggerResource();
                swaggerResource.setUrl(url);
                swaggerResource.setSwaggerVersion("2.0");
                swaggerResource.setName(instance);
                // Swagger排除监控
                if (!instance.contains("cloud-monitor")) {
                    resources.add(swaggerResource);
                }
            }
        });
        return resources;
    }

}
