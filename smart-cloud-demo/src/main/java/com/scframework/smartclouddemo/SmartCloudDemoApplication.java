package com.scframework.smartclouddemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author sonin
 * @date 2021/8/27 8:12
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.scframework.smartclouddemo"})
public class SmartCloudDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCloudDemoApplication.class, args);
    }

}
