package com.scframework.smartclouddemo.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.scframework.smartclouddemo.entity.User;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sonin
 * @date 2021/9/4 14:20
 */
@RestController
@RequestMapping("/user")
@Api(tags = "User接口")
public class UserController {

    @GetMapping("/info")
    public Object userInfo() {
        return StpUtil.getSession().get("userInfo");
    }

}
