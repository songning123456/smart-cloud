package com.scframework.smartcloudgateway.nacos.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.listener.Listener;
import com.scframework.smartcloudgateway.route.service.IDynamicRouteCrud;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author sonin
 * @date 2021/8/26 9:51
 */
@Slf4j
@Component
public class GatewayRouteListener implements Listener {

    @Autowired
    private IDynamicRouteCrud dynamicRouteService;

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public void receiveConfigInfo(String val1) {
        log.info("进行网关更新:\n\r{}", val1);
        List<RouteDefinition> definitionList = JSON.parseArray(val1, RouteDefinition.class);
        for (RouteDefinition definition : definitionList) {
            log.info("update route: {}", definition.toString());
            dynamicRouteService.update(definition);
        }
    }

}
