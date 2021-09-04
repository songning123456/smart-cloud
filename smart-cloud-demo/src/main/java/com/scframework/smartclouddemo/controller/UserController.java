package com.scframework.smartclouddemo.controller;

import com.scframework.smartclouddemo.component.LoginUserHolder;
import com.scframework.smartclouddemo.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private LoginUserHolder loginUserHolder;

    @GetMapping("/current-user")
    @ApiOperation(value = "User-获取当前用户信息", notes = "User-获取当前用户信息")
    public User currentUserCtrl() {
        return loginUserHolder.getCurrentUser();
    }

}
