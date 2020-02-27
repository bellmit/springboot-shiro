package com.sq.transportmanage.gateway.api;

import com.sq.transportmanage.gateway.api.web.filter.AccessFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * @program: mp-manage-boot
 * @description: 启动类
 * @author: zjw
 * @create: 2020-02-15 23:06
 **/
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = "com.sq.transportmanage.gateway")
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

}