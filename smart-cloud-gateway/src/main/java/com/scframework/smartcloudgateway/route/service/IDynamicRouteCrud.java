package com.scframework.smartcloudgateway.route.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author sonin
 * @date 2021/8/21 8:17
 * 动态更新路由网关service
 * 1. 实现一个Spring提供的事件推送接口ApplicationEventPublisherAware
 * 2. 提供动态路由的基础方法，可通过获取bean操作该类的方法。该类提供新增路由、更新路由、删除路由，然后实现发布的功能。
 * *
 */
@Slf4j
@Component
public class IDynamicRouteCrud implements ApplicationEventPublisherAware {

    @Autowired
    private InMemoryRouteDefinitionRepository repository;

    /**
     * 发布事件
     */
    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 删除路由
     *
     * @param id
     */
    private synchronized void delete(String id) {
        this.repository.delete(Mono.just(id)).then(Mono.defer(() -> Mono.just(ResponseEntity.ok().build()))).onErrorResume((t) -> t instanceof NotFoundException, (t) -> Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * 更新路由
     *
     * @param definition
     */
    public synchronized void update(RouteDefinition definition) {
        log.info("gateway prepare update route {}", definition);
        try {
            delete(definition.getId());
            repository.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            log.error("gateway update route error: {}", e.getMessage());
        }
    }

    /**
     * 增加路由
     *
     * @param definition
     */
    public synchronized void add(RouteDefinition definition) {
        log.info("gateway prepare add route {}", definition);
        try {
            repository.save(Mono.just(definition)).subscribe();
        } catch (Exception e) {
            log.error("gateway add route error: {}", e.getMessage());
        }
    }

}
