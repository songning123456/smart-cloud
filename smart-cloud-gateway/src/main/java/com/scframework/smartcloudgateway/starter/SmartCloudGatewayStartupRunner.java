package com.scframework.smartcloudgateway.starter;

import com.scframework.smartcloudgateway.loader.DynamicRouteLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author sonin
 * @date 2021/8/21 11:25
 */
@Component
@Slf4j
public class SmartCloudGatewayStartupRunner implements CommandLineRunner {

    @Resource
    private DynamicRouteLoader dynamicRouteLoader;

    /**
     * 容器初始化后加载路由
     *
     * @param args
     */
    @Override
    public void run(String... args) {
        try {
            dynamicRouteLoader.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("SmartCloudGatewayStartup启动error: {}", e.getMessage());
        }
    }

}
