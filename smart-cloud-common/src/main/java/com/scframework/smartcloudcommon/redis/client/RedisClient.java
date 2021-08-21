package com.scframework.smartcloudcommon.redis.client;

import com.scframework.smartcloudcommon.constant.GlobalConstant;
import com.scframework.smartcloudcommon.entity.BaseMap;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author sonin
 * @date 2021/8/21 15:34
 * redis客户端
 */
@Configuration
public class RedisClient {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 发送消息
     *
     * @param handlerName
     * @param params
     */
    public void sendMessage(String handlerName, BaseMap params) {
        params.put(GlobalConstant.HANDLER_NAME, handlerName);
        redisTemplate.convertAndSend(GlobalConstant.REDIS_TOPIC_NAME, params);
    }

}
