package com.scframework.smartcloudoauth2.entity;

import lombok.Data;

import java.util.List;

/**
 * @author sonin
 * @date 2021/9/5 18:57
 */
@Data
public class User {

    private Integer id;

    private String username;

    private String password;

    private List<String> permissionList;
}
