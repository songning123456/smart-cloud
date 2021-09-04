package com.scframework.smartclouddemo.component;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import com.scframework.smartclouddemo.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sonin
 * @date 2021/9/4 14:17
 */
@Component
public class LoginUserHolder {

    public User getCurrentUser() {

        // 从Header中获取用户信息
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String userStr = request.getHeader("user");
        JSONObject userJsonObject = new JSONObject(userStr);
        User user = new User();
        user.setUsername(userJsonObject.getStr("user_name"));
        user.setId(Convert.toLong(userJsonObject.get("id")));
        user.setRoles(Convert.toList(String.class, userJsonObject.get("authorities")));
        return user;
    }
}
