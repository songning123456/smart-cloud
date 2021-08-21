package com.scframework.smartcloudcommon.entity;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author sonin
 * @date 2021/8/21 15:18
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候中取出ApplicationContext.
 */
@Slf4j
public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的context注入函数，将其存入静态变量
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContext.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean，自动转型为所赋值对象的类型
     */
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean，自动转型为所赋值对象的类型
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        T tObject = null;
        if (ObjectUtil.isNotEmpty(name)) {
            checkApplicationContext();
            try {
                tObject = applicationContext.getBean(name, clazz);
            } catch (Exception e) {
                log.error(name + "未定义！");
            }
        }
        return tObject;
    }


    /**
     * 从静态变量ApplicationContext中取得Bean，自动转型为所赋值对象的类型
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(clazz);
    }

    /**
     * 清除applicationContext静态变量
     */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicationContext未注入，请在applicationContext.xml中定义SpringApplicationContext");
        }
    }

}
