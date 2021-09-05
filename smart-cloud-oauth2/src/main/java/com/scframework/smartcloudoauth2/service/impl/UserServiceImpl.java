package com.scframework.smartcloudoauth2.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.scframework.smartcloudoauth2.constant.UserConstant;
import com.scframework.smartcloudoauth2.service.UserService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sonin
 * @date 2021/9/5 15:31
 */
@Service
public class UserServiceImpl implements UserService {

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
        List<Map<String, Object>> findUserList = UserConstant.USERS.stream().filter(item -> item.get("username").equals(username)).collect(Collectors.toList());
        if (findUserList.isEmpty()) {
            return null;
        }
        return findUserList.get(0);
    }

}
