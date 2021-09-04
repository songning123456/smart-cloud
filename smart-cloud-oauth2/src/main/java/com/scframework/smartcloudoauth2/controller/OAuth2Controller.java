package com.scframework.smartcloudoauth2.controller;


import com.scframework.smartcloudoauth2.api.CommonResult;
import com.scframework.smartcloudoauth2.entity.Oauth2Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

/**
 * @author sonin
 * @date 2021/9/4 13:32
 */
@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    /**
     * oauth2登录认证
     */
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public CommonResult<Oauth2Token> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        assert oAuth2AccessToken != null;
        Oauth2Token oauth2Token = Oauth2Token.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .tokenHead("Bearer ").build();

        return CommonResult.success(oauth2Token);
    }

}
