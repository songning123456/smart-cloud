package com.scframework.smartcloudgateway.entity;

import lombok.Data;

/**
 * @author sonin
 * @date 2021/8/20 21:23
 */
@Data
public class GatewayRoute {

    private String id;
    private String name;
    private String uri;
    private String predicates;
    private String filters;
    private Integer stripPrefix;
    private Integer retryable;
    private Integer persist;
    private Integer status;

}
