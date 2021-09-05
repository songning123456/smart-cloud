package com.scframework.smartcloudgateway.oauth2.config;

import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author sonin
 * @date 2021/9/5 16:32
 */
@Configuration
@DependsOn("ignoreUrlsConfig")
public class SaTokenConfig {

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    /**
     * 注册Sa-Token全局过滤器
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        SaReactorFilter saReactorFilter = new SaReactorFilter();
        // 拦截地址
        saReactorFilter.addInclude("/**");
        // 开放地址
        for (String url : ignoreUrlsConfig.getUrls()) {
            saReactorFilter.addExclude(url);
        }
        // 鉴权方法：每次访问进入
        saReactorFilter.setAuth(r -> {
            // 登录认证：除登录接口都需要认证
            SaRouter.match("/**", "/smart-cloud-oauth2/user/login", StpUtil::checkLogin);
            // 权限认证：不同接口访问权限不同
            SaRouter.match("/smart-cloud-demo/test/hello", () -> StpUtil.checkPermission("smart-cloud-demo:test:hello"));
            SaRouter.match("/smart-cloud-demo/user/info", () -> StpUtil.checkPermission("smart-cloud-demo:user:info"));
        });
        // setAuth方法异常处理
        saReactorFilter.setError(e -> {
            //设置错误返回格式为JSON
            ServerWebExchange exchange = SaReactorSyncHolder.getContent();
            exchange.getResponse().getHeaders().set("Content-Type", "application/json; charset=utf-8");
            return SaResult.error(e.getMessage());
        });
        return saReactorFilter;
    }

}
