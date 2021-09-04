package com.scframework.smartcloudoauth2.api;

/**
 * @author sonin
 * @date 2021/9/4 13:33
 * 封装API的错误码
 */
public interface IErrorCode {
    long getCode();

    String getMessage();
}

