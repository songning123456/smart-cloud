package com.scframework.smartcloudoauth2.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.scframework.smartcloudoauth2.vo.Result;
import com.scframework.smartcloudoauth2.service.UserService;
import com.scframework.smartcloudoauth2.vo.SaTokenInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sonin
 * @date 2021/9/5 15:29
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * x-www-form.urlencoded 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return Result<SaTokenInfoVO>
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<SaTokenInfoVO> login(@RequestParam String username, @RequestParam String password) {
        Result<SaTokenInfoVO> result = new Result<>();
        try {
            SaTokenInfo saTokenInfo = userService.login(username, password);
            if (saTokenInfo == null) {
                return result.error500("用户名或密码错误");
            }
            result.setResult(SaTokenInfoVO.builder().tokenName(saTokenInfo.getTokenName()).tokenValue(saTokenInfo.getTokenValue()).build());
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage());
            result.error500(e.getMessage());
        }
        return result;
    }

}
