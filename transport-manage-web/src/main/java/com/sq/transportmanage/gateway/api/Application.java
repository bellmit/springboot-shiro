package com.sq.transportmanage.gateway.api;

import com.sq.transportmanage.gateway.api.web.filter.AccessFilter;
import com.sq.transportmanage.gateway.api.web.filter.TraceIdFilter;
import com.sq.transportmanage.gateway.api.web.interceptor.LoginoutListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @program: mp-manage-boot
 * @description: 启动类
 * @author: zjw
 * @create: 2020-02-15 23:06
 **/
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.sq.transportmanage.gateway","mp.mvc.logger"})
//@ServletComponentScan(basePackageClasses = {TraceIdFilter.class})
@ServletComponentScan(basePackageClasses = {LoginoutListener.class})
@EnableZuulProxy
@MapperScan("com.sq.transportmanage.gateway.dao.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 允许cookies跨域
        config.addAllowedOrigin("*");// #允许向该服务器提交请求的URI，*表示全部允许
        config.addAllowedHeader("*");// #允许访问的头信息,*表示全部
        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.addAllowedMethod("*");// 允许提交请求的方法，*表示全部允许，也可以单独设置GET、PUT等
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}