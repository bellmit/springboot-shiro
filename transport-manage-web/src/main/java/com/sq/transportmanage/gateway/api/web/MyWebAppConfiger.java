package com.sq.transportmanage.gateway.api.web;

import com.sq.transportmanage.gateway.api.web.interceptor.HttpParamVerifyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MyWebAppConfiger implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedMethods("POST","GET","OPTIONS","DELETE","PUT")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .maxAge(3600);

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
         registry.addInterceptor(new HttpParamVerifyInterceptor());
    }


}
