package com.sq.transportmanage.gateway.api.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.util.IntrospectorCleanupListener;

import javax.servlet.http.HttpSessionAttributeListener;
import java.awt.*;
import java.util.EventListener;

/**
 * @Author fanht
 * @Description  让参数拦截器生效
 * @Date 2020/2/28 下午6:44
 * @Version 1.0
 */
@Slf4j
@SpringBootConfiguration
@Configuration
public class SpringMVCConfig extends WebMvcConfigurationSupport {
    @Autowired
    private HttpParamVerifyInterceptor httpParamVerifyInterceptor;

/*    @Autowired
    private LoginoutListener loginoutListener;*/

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpParamVerifyInterceptor).addPathPatterns("/**");
    }



    /*@Bean
    public ServletListenerRegistrationBean listenerRegistrationBean(){

        ServletListenerRegistrationBean srb = new ServletListenerRegistrationBean();
        //防止Spring内存溢出监听器
        srb.setListener(new IntrospectorCleanupListener());
        srb.setListener(new LoginoutListener());
        //request监听器 主要需要配置这个监听器
        srb.setListener(new RequestContextListener());
        log.info("==========listen start=======");
        return srb;
    }*/
}
