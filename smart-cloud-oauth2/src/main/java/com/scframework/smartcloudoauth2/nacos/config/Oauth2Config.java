package com.scframework.smartcloudoauth2.nacos.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author sonin
 * @date 2021/9/5 17:59
 */
@Slf4j
@Configuration
public class Oauth2Config {

    public static final long DEFAULT_TIMEOUT = 30000;

    public static String SERVER_ADDR;

    public static String USERNAME;

    public static String PASSWORD;

    public static String OAUTH2_DATA_ID;

    public static String OAUTH2_GROUP;

    @Value("${smart.cloud.nacos.server-addr}")
    public void setServerAddr(String serverAddr) {
        SERVER_ADDR = serverAddr;
    }

    @Value("${smart.cloud.nacos.username}")
    public void setUsername(String username) {
        USERNAME = username;
    }

    @Value("${smart.cloud.nacos.password}")
    public void setPassword(String password) {
        PASSWORD = password;
    }

    @Value("${smart.cloud.oauth2.config.data-id:#{null}}")
    public void setOauth2DataId(String dataId) {
        OAUTH2_DATA_ID = dataId + ".json";
    }

    @Value("${smart.cloud.oauth2.config.group:DEFAULT_GROUP:#{null}}")
    public void setOauth2Group(String oauth2Group) {
        OAUTH2_GROUP = oauth2Group;
    }

}
