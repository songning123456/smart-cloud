package com.scframework.smartclouddemo.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author sonin
 * @date 2021/8/27 8:17
 */
@Data
@ApiModel(value="User对象", description="User对象")
@EqualsAndHashCode(callSuper = false)
public class User {
    private Long id;
    private String username;
    private String password;
    private List<String> roles;
}
