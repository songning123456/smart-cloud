package com.scframework.smartcloudoauth2.constant;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.scframework.smartcloudoauth2.entity.User;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author sonin
 * @date 2021/9/5 17:56
 */
public class UserConstant {

    public static List<Map<String, Object>> USERS = new CopyOnWriteArrayList<>();

    public static void convert(List<User> userList) {
        USERS.clear();
        Map<String, Object> userMap;
        for (User user : userList) {
            userMap = new HashMap<>(2);
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("password", SaSecureUtil.md5(user.getPassword()));
            userMap.put("permissionList", user.getPermissionList());
            USERS.add(userMap);
        }
    }

}
