package com.sq.transportmanage.gateway.api.config;

//import com.sq.mp.carmanage.api.interceptor.CharacterValidInterceptors;
//import com.sq.mp.carmanage.api.interceptor.HttpParamVerifyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author
 * @Date: 2019/5/8 17:25
 * @Description:(注册拦截器)
 */
@Configuration
public class MyWebAppConfiger extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new CharacterValidInterceptors()).addPathPatterns("/**");
//        registry.addInterceptor(new HttpParamVerifyInterceptor()).addPathPatterns("/**");
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
