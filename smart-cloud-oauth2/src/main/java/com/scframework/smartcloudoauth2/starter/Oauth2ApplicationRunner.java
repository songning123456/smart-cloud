package com.scframework.smartcloudoauth2.starter;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.scframework.smartcloudoauth2.constant.UserConstant;
import com.scframework.smartcloudoauth2.entity.User;
import com.scframework.smartcloudoauth2.nacos.config.NacosConfig;
import com.scframework.smartcloudoauth2.nacos.config.Oauth2Config;
import com.scframework.smartcloudoauth2.nacos.listener.Oauth2Listener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sonin
 * @date 2021/9/5 18:06
 */
@Slf4j
@Component
public class Oauth2ApplicationRunner implements ApplicationRunner {

    @Autowired
    private Oauth2Listener oauth2Listener;

    @Override
    public void run(ApplicationArguments args) {
        log.info("~~~从nacos读取用户信息~~~");
        this.loadUsersByNacos();
    }

    private void loadUsersByNacos() {
        NacosConfig nacosConfig = new NacosConfig();
        ConfigService configService = nacosConfig.createOauth2Config();
        if (configService == null) {
            log.warn("init oauth2 config fail!");
        }
        try {
            assert configService != null;
            String configInfo = configService.getConfig(Oauth2Config.OAUTH2_DATA_ID, Oauth2Config.OAUTH2_GROUP, Oauth2Config.DEFAULT_TIMEOUT);
            if (StringUtils.isNotBlank(configInfo)) {
                log.info("获取Oauth2-users当前配置:\r\n{}", configInfo);
                List<User> userList = JSON.parseArray(configInfo, User.class);
                UserConstant.convert(userList);
            }
        } catch (NacosException e) {
            log.error("初始化Oauth2发生错误: {}", e.getMessage());
        }
        // 监听nacos网关路由变化
        try {
            configService.addListener(Oauth2Config.OAUTH2_DATA_ID, Oauth2Config.OAUTH2_GROUP, oauth2Listener);
        } catch (Exception e) {
            log.error("从nacos接收Oauth2配置出错: {}", e.getMessage());
        }
    }

}
