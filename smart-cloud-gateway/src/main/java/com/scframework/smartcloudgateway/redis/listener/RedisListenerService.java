package com.scframework.smartcloudgateway.redis.listener;


import com.scframework.smartcloudgateway.entity.BaseMap;

/**
 * @author sonin
 * @date 2021/8/20 21:54
 * 自定义消息监听
 */
public interface RedisListenerService {

    void onMessage(BaseMap message);
}
