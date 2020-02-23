package com.sq.transportmanage.gateway.api;


import com.sq.transportmanage.gateway.service.common.datasource.DataSourceConfig;
import com.sq.transportmanage.gateway.service.shiro.PlatformShiroFilterFactoryBean;
import com.sq.transportmanage.gateway.service.shiro.cache.RedisCacheManager;
import com.sq.transportmanage.gateway.service.shiro.realm.UsernamePasswordRealm;
import com.sq.transportmanage.gateway.service.shiro.session.RedisSessionDAO;
import com.sq.transportmanage.gateway.service.shiro.session.UuIdSessionIdGenerator;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author:qxx
 * @Date:2019/7/3
 * @Description:
 */

@Configuration
@AutoConfigureAfter(DataSourceConfig.class)
public class ShiroConfiguration {

    @Value(value = "${login.url}")
    private String loginUrl;


    @Resource(name = "ncdsSerRedisTemplate")
    private RedisTemplate ncdsSerRedisTemplate;

    @Bean(name = "shiroCacheManager")
    public RedisCacheManager shiroCacheManager() {
        RedisCacheManager shiroCacheManager = new RedisCacheManager();
        shiroCacheManager.setRedisTemplate(ncdsSerRedisTemplate);
        shiroCacheManager.setExpireSeconds(1800);
        return shiroCacheManager;
    }


    @Bean(name = "sessionIdGenerator")
    public UuIdSessionIdGenerator sessionIdGenerator() {
        UuIdSessionIdGenerator sessionIdGenerator = new UuIdSessionIdGenerator();
        return sessionIdGenerator;
    }

    @Bean(name = "sessionIdCookie")
    public SimpleCookie sessionIdCookie() {
        SimpleCookie sessionIdCookie = new SimpleCookie("sidconf");
        sessionIdCookie.setHttpOnly(true);
        sessionIdCookie.setMaxAge(-1);
        return sessionIdCookie;
    }

    @Bean(name = "sessionDAO")
    public RedisSessionDAO sessionDAO(UuIdSessionIdGenerator sessionIdGenerator, UsernamePasswordRealm shiroRealm) {
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        sessionDAO.setSessionIdGenerator(sessionIdGenerator);
        sessionDAO.setRedisTemplate(ncdsSerRedisTemplate);
        sessionDAO.setAuthorizingRealm(shiroRealm);
        return sessionDAO;
    }


    @Bean
    public UsernamePasswordRealm shiroRealm() {
        UsernamePasswordRealm shiroRealm = new UsernamePasswordRealm();
        shiroRealm.setName("ConferenceUsernamePasswordRealm");
        shiroRealm.setAuthenticationCachingEnabled(true);
        return shiroRealm;
    }


    @Bean("sessionManager")
    public DefaultWebSessionManager sessionManager(RedisSessionDAO sessionDAO, SimpleCookie sessionIdCookie) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //session存活时间60分钟
        sessionManager.setGlobalSessionTimeout(3600000);
        sessionManager.setDeleteInvalidSessions(true);
        //sessionManager.setSessionValidationSchedulerEnabled(true);
        //sessionManager.setSessionValidationScheduler(sessionValidationScheduler);
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie);
        return sessionManager;
    }

    @Bean(name = "rememberMeCookie")
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("sqRememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(432000);
        return simpleCookie;
    }


    @Bean(name = "rememberMeManager")
    public CookieRememberMeManager rememberMeManager(SimpleCookie rememberMeCookie) {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        byte[] cipherKey = Base64.getEncoder().encode("4AvVhmFLUs0KTA3Kprsdag==".getBytes());
        rememberMeManager.setCipherKey(cipherKey);
        rememberMeManager.setCookie(rememberMeCookie);
        return rememberMeManager;
    }


    @Bean
    public DefaultWebSecurityManager securityManager(RedisCacheManager shiroCacheManager,
                                                     CookieRememberMeManager rememberMeManager,
                                                     UsernamePasswordRealm shiroRealm,
                                                     DefaultWebSessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);

        securityManager.setCacheManager(shiroCacheManager);
        securityManager.setSessionManager(sessionManager);
        // 指定 SubjectFactory,如果要实现cas的remember me的功能，需要用到下面这个CasSubjectFactory，并设置到securityManager的subjectFactory中
        //securityManager.setSubjectFactory(new CasSubjectFactory());
        securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }

    //
    //@Bean
    //public FilterRegistrationBean delegatingFilterProxy(){
    //    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    //    DelegatingFilterProxy proxy = new DelegatingFilterProxy();
    //    proxy.setTargetFilterLifecycle(true);
    //    proxy.setTargetBeanName("shiroFilter");
    //    filterRegistrationBean.setFilter(proxy);
    //    return filterRegistrationBean;
    //}


    @Bean(name = "shiroFilter")
    public PlatformShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        PlatformShiroFilterFactoryBean shiroFilterFactoryBean = new PlatformShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        shiroFilterFactoryBean.setSuccessUrl("${homepage.url}");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized.json");

//        Map<String, Filter> filters = new HashMap<>();
//        //filters.put("casFilter", casFilter);
//        shiroFilterFactoryBean.setFilters(filters);
        //注意此处使用的是LinkedHashMap，是有顺序的，shiro会按从上到下的顺序匹配验证，匹配了就不再继续验证
        //所以上面的url要苛刻，宽松的url要放在下面，尤其是"/**"要放到最下面，如果放前面的话其后的验证规则就没作用了。
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/nginx.html", "anon");
        filterChainDefinitionMap.put("/common/filesUpload", "anon");
        filterChainDefinitionMap.put("/dispatcher/changeStatus", "anon");
        filterChainDefinitionMap.put("/driver/getUnderWayDriver", "anon");
        filterChainDefinitionMap.put("/login.json", "anon");
        filterChainDefinitionMap.put("/updateLevel.json", "anon");
        filterChainDefinitionMap.put("/permission/levelList.json", "anon");
        filterChainDefinitionMap.put("/unauthorized.json", "anon");
        filterChainDefinitionMap.put("/getMsgCode.json", "anon");
        filterChainDefinitionMap.put("/dologin.json", "anon");
        filterChainDefinitionMap.put("/logout.html", "logout");
        filterChainDefinitionMap.put("/**", "user");
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
}

