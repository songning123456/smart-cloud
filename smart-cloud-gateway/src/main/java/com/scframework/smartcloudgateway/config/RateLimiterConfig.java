package com.scframework.smartcloudgateway.config;

import com.scframework.smartcloudgateway.filter.GlobalAccessTokenFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author sonin
 * @date 2021/8/20 21:59
 * 路由限流配置
 */
@Slf4j
@Configuration
public class RateLimiterConfig {

    /**
     * IP限流 (通过exchange对象可以获取到请求信息，这边用了hostName)
     */
    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
    }

    /**
     * 用户限流 (通过exchange对象可以获取到请求信息，获取当前请求的用户TOKEN)
     */
    @Bean
    public KeyResolver userKeyResolver() {
        // 使用这种方式限流，请求Header中必须携带X-Access-Token参数
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getHeaders().getFirst(GlobalAccessTokenFilter.X_ACCESS_TOKEN)));
    }

    /**
     * 接口限流 (获取请求地址的uri作为限流key)
     */
    @Bean
    public KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

}
