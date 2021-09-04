package com.scframework.smartcloudoauth2.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sonin
 * @date 2021/9/4 13:35
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Oauth2Token {
    /**
     * 访问令牌
     */
    private String token;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 访问令牌头前缀
     */
    private String tokenHead;
    /**
     * 有效时间（秒）
     */
    private int expiresIn;
}

