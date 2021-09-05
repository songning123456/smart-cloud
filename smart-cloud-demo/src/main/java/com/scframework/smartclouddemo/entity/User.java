package com.scframework.smartclouddemo.entity;

import lombok.*;

import java.util.List;

/**
 * @author sonin
 * @date 2021/9/5 15:33
 * 用户信息类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String username;
    private String password;
    private List<String> permissionList;

}
