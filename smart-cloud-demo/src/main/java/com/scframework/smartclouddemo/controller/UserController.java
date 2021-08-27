package com.scframework.smartclouddemo.controller;

import com.scframework.smartclouddemo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sonin
 * @date 2021/8/27 8:16
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {


    @GetMapping("/getById")
    public User getUserCtrl(@RequestParam(value = "id") String id) {
        User user = new User();
        user.setId(id);
        user.setName("sonin");
        user.setAge(26);
        user.setSex("男");
        return user;
    }

}
