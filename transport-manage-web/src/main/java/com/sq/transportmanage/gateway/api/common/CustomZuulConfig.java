//package com.sq.mp.carmanage.api.common;
//
//import com.sq.mp.carmanage.api.zuul.CustomRouteLocator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.web.ServerProperties;
//import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Created by xujingfeng on 2017/4/1.
// */
//@Configuration
//public class CustomZuulConfig {
//
//    @Autowired
//    ZuulProperties zuulProperties;
//    @Autowired
//    ServerProperties server;
////    @Autowired
////    JdbcTemplate jdbcTemplate;
//
//    @Bean
//    public CustomRouteLocator routeLocator() {
////        CustomRouteLocator routeLocator = new CustomRouteLocator(this.server.getServlet().getContextPath(), this.zuulProperties);
////        routeLocator.setJdbcTemplate(jdbcTemplate);
////        return routeLocator;
//        return null;
//
//    }
//
//}
