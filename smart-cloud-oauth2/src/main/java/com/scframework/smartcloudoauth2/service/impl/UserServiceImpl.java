package com.scframework.smartcloudoauth2.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.scframework.smartcloudoauth2.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sonin
 * @date 2021/9/5 15:31
 */
@Service
public class UserServiceImpl implements UserService {

    private List<Map<String, Object>> userList;

    @PostConstruct
    public void initData() {
        userList = new ArrayList<>();
        Map<String, Object> userMap1 = new HashMap<>(2);
        userMap1.put("id", 1L);
        userMap1.put("username", "admin");
        userMap1.put("password", SaSecureUtil.md5("123456"));
        userMap1.put("permissionList", Arrays.asList("smart-cloud-demo:user:info", "smart-cloud-demo:test:hello"));
        userList.add(userMap1);
        Map<String, Object> userMap2 = new HashMap<>(2);
        userMap2.put("id", 2L);
        userMap2.put("username", "macro");
        userMap2.put("password", SaSecureUtil.md5("123456"));
        userMap2.put("permissionList", Collections.singletonList("smart-cloud-demo:user:info"));
        userList.add(userMap2);
    }

    @Override
    public SaTokenInfo login(String username, String password) {
        SaTokenInfo saTokenInfo;
        Map<String, Object> user = loadUserByUsername(username);
        if (user == null) {
            return null;
        }
        if (!SaSecureUtil.md5(password).equals(user.get("password"))) {
            return null;
        }
        // 密码校验成功后登录，一行代码实现登录
        StpUtil.login(user.get("id"));
        // 将用户信息存储到Session中
        StpUtil.getSession().set("userInfo", user);
        // 获取当前登录用户Token信息
        saTokenInfo = StpUtil.getTokenInfo();
        return saTokenInfo;
    }

    private Map<String, Object> loadUserByUsername(String username) {
        List<Map<String, Object>> findUserList = userList.stream().filter(item -> item.get("username").equals(username)).collect(Collectors.toList());
        if (findUserList.isEmpty()) {
            return null;
        }
        return findUserList.get(0);
    }

}
