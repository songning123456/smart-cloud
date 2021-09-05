package com.scframework.smartcloudoauth2.service;

import cn.dev33.satoken.stp.SaTokenInfo;

/**
 * @author sonin
 * @date 2021/9/5 15:32
 */
public interface UserService {

    SaTokenInfo login(String username, String password);

}
