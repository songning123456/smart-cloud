package com.scframework.smartclouddemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sonin
 * @date 2021/8/27 8:21
 */
@RestController
@RequestMapping("/ip-port")
@Slf4j
public class IpPortController {

    @RequestMapping("/get")
    private String getIpPortCtrl() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        String localAddr = request.getLocalAddr();
        int serverPort = request.getServerPort();
        log.info("当前currentTime=> {}, ip:port=> {}:{}", System.currentTimeMillis(), localAddr, serverPort);
        return localAddr + ":" + serverPort;
    }

}
