package com.scframework.smartcloudgateway.config;

import com.scframework.smartcloudgateway.entity.SpringApplicationContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sonin
 * @date 2021/8/21 15:16
 */
@Configuration
public class CommonConfig {

    /**
     * Spring上下文工具配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SpringApplicationContext.class)
    public SpringApplicationContext springContextHolder() {
        return new SpringApplicationContext();
    }

}
