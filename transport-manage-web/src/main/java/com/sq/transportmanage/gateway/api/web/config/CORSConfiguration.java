package com.sq.transportmanage.gateway.api.web.config;

/**
 * @program: transport-manage-gateway
 * @description:
 * @author: zjw
 * @create: 2020-03-12 21:22
 **/

import com.sq.transportmanage.gateway.api.web.interceptor.HttpParamVerifyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 解决跨域问题springboot所需配置
 */
@Configuration
public class CORSConfiguration  extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new CharacterValidInterceptors()).addPathPatterns("/**");
        registry.addInterceptor(new HttpParamVerifyInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowCredentials(true);
    }

}
