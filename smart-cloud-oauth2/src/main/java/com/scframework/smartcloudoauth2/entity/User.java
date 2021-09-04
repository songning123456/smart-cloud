package com.scframework.smartcloudoauth2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author sonin
 * @date 2021/9/4 10:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String password;
    private Integer status;
    private List<String> roles;

}
