package com.sq.transportmanage.gateway.api.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sq.transportmanage.gateway.service.common.shiro.session.RedisSessionDAO;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author fanht
 * @Description 监听当前有哪些用户，当收到特定通知后通知退出登录
 * @Date 2020/3/5 下午1:48
 * @Version 1.0
 */
@Component
public class LoginoutListener extends RedisSessionDAO   implements SessionListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public  static final Map<Long,String> mapUser = Maps.newHashMap();
    public final static AtomicInteger sessionCount = new AtomicInteger(0);

    @Override
    public void onStart(Session session) {
        //会话创建，在线人数加一
        logger.info("======" + sessionCount);
        sessionCount.incrementAndGet();
    }

    @Override
    public void onStop(Session session) {
        //会话退出,在线人数减一
        sessionCount.decrementAndGet();
    }

    @Override
    public void onExpiration(Session session) {
        //会话过期,在线人数减一
        sessionCount.decrementAndGet();

    }


    /**
     * 获取在线人数使用
     * @return
     */
    public AtomicInteger getSessionCount() {
        return sessionCount;
    }



    /*@Override
    public void sessionCreated(HttpSessionEvent se) {
        onlineCount++;
        logger.info("创建start====== ===" + se.getSession().getId());
        mapUser.put(se.getSession().getCreationTime(),se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("销毁session=============");
    }*/
}
