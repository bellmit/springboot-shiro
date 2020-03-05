package com.sq.transportmanage.gateway.api.web.interceptor;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;

/**
 * @Author fanht
 * @Description 监听当前有哪些用户，当收到特定通知后通知退出登录
 * @Date 2020/3/5 下午1:48
 * @Version 1.0
 */
@Component
public class LoginoutListener implements HttpSessionListener  {

    public static Map<Integer,String> mapUser = Maps.newHashMap();

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
    }
}
