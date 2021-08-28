package com.scframework.smartclouddemo.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author sonin
 * @date 2021/8/27 8:17
 */
@Data
@ApiModel(value="User对象", description="User对象")
public class User {

    private String id;

    private String name;

    private Integer age;

    private String sex;
}
