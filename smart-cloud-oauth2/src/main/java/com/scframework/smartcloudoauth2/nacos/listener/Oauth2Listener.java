package com.scframework.smartcloudoauth2.nacos.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.listener.Listener;
import com.scframework.smartcloudoauth2.constant.UserConstant;
import com.scframework.smartcloudoauth2.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author sonin
 * @date 2021/9/5 18:02
 */
@Component
@Slf4j
public class Oauth2Listener implements Listener {

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public void receiveConfigInfo(String val0) {
        log.info("进行Oauth2-users更新:\n\r{}", val0);
        List<User> userList = JSON.parseArray(val0, User.class);
        UserConstant.convert(userList);
    }

}
