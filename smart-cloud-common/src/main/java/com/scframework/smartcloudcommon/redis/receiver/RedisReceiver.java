package com.scframework.smartcloudcommon.redis.receiver;

import cn.hutool.core.util.ObjectUtil;
import com.scframework.smartcloudcommon.constant.GlobalConstant;
import com.scframework.smartcloudcommon.entity.BaseMap;
import com.scframework.smartcloudcommon.entity.SpringApplicationContext;
import com.scframework.smartcloudcommon.redis.listener.RedisListenerService;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author sonin
 * @date 2021/8/21 15:43
 */
@Data
@Component
public class RedisReceiver {

    /**
     * 接受消息并调用业务逻辑处理器
     *
     * @param params
     */
    public void onMessage(BaseMap params) {
        Object handlerName = params.get(GlobalConstant.HANDLER_NAME);
        RedisListenerService messageListener = SpringApplicationContext.getBean(handlerName.toString(), RedisListenerService.class);
        if (ObjectUtil.isNotEmpty(messageListener)) {
            messageListener.onMessage(params);
        }
    }

}
