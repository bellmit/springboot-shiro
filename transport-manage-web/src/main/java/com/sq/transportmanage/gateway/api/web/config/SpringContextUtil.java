package com.sq.transportmanage.gateway.api.web.config;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author (yangbo)
 * @Date: 2019/4/1 14:43
 * @Description:(springcontextutil工具类)
 */
@Component
public class SpringContextUtil implements ApplicationContextAware{

    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /***
     * 获取当前环境
     * @return String
     */
    public static String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }
}
