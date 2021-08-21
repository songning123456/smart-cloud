package com.scframework.smartcloudgateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author sonin
 * @date 2021/8/20 21:19
 * 响应超时熔断处理器
 */
@RestController
public class FallbackController {

    /**
     * 全局熔断处理
     */
    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("访问超时，请稍后再试!");
    }

    /**
     * demo熔断处理
     */
    @RequestMapping("/demo-fallback")
    public Mono<String> demoFallback() {
        return Mono.just("访问超时，请稍后再试!");
    }
}
