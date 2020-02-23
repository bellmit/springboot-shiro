package com.sq.transportmanage.gateway.service.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SpringBeanFactory implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanFactory.CONTEXT = applicationContext;
    }

    public static <T> T getBean(Class<T> cz) {
        return CONTEXT.getBean(cz);
    }

    public static <T> T getBean(String beanName) {
        return (T) CONTEXT.getBean(beanName);
    }

    public static <T> Map<String, T> beansOfTypeIncludingAncestors(Class<T> type) {
        return BeanFactoryUtils.beansOfTypeIncludingAncestors(CONTEXT, type, false, false);
    }
}
