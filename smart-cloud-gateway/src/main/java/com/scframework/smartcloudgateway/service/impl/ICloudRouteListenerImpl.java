package com.scframework.smartcloudgateway.service.impl;

import com.scframework.smartcloudcommon.entity.BaseMap;
import com.scframework.smartcloudcommon.service.ICloudRouteListener;
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
public class ICloudRouteListenerImpl implements ICloudRouteListener {

    @Resource
    private DynamicRouteLoader dynamicRouteLoader;

    @Override
    public void onMessage(BaseMap message) {
        dynamicRouteLoader.refresh();
    }

}
