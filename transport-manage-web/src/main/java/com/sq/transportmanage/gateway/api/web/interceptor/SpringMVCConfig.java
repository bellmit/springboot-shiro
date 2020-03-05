package com.sq.transportmanage.gateway.api.web.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Author fanht
 * @Description  让参数拦截器生效
 * @Date 2020/2/28 下午6:44
 * @Version 1.0
 */
@SpringBootConfiguration
public class SpringMVCConfig extends WebMvcConfigurationSupport {
    @Autowired
    private HttpParamVerifyInterceptor httpParamVerifyInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpParamVerifyInterceptor).addPathPatterns("/**");
    }
}
