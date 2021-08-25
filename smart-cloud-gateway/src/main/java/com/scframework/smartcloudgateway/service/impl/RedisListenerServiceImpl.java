package com.scframework.smartcloudgateway.service.impl;

import com.scframework.smartcloudgateway.entity.BaseMap;
import com.scframework.smartcloudgateway.redis.listener.RedisListenerService;
import com.scframework.smartcloudgateway.loader.DynamicRouteLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author sonin
 * @date 2021/8/21 10:49
 * 路由刷新监听
 */
@Slf4j
@Component
public class RedisListenerServiceImpl implements RedisListenerService {

    @Resource
    private DynamicRouteLoader dynamicRouteLoader;

    @Override
    public void onMessage(BaseMap message) {
        dynamicRouteLoader.refresh();
    }

}
