package com.scframework.smartcloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author sonin
 * @date 2021/8/26 14:37
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.scframework.smartcloudgateway"})
public class SmartCloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCloudGatewayApplication.class, args);
    }

}
