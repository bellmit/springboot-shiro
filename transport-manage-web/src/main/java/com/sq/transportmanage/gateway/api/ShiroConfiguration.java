package com.sq.transportmanage.gateway.api;


import com.sq.transportmanage.gateway.api.web.interceptor.LoginoutListener;
import com.sq.transportmanage.gateway.service.common.datasource.DataSourceConfig;
import com.sq.transportmanage.gateway.service.common.shiro.PlatformShiroFilterFactoryBean;
import com.sq.transportmanage.gateway.service.common.shiro.cache.RedisCacheManager;
import com.sq.transportmanage.gateway.service.common.shiro.realm.UsernamePasswordRealm;
import com.sq.transportmanage.gateway.service.common.shiro.session.RedisSessionDAO;
import com.sq.transportmanage.gateway.service.common.shiro.session.UuIdSessionIdGenerator;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
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
import java.util.*;

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

    @Value(value = "${unauthorized.url}")
    private String unauthorizedUrl;

    @Value(value = "${homepage.url}")
    private String homepageUrl;


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
        //<!-- 是否启用授权缓存（生产环境可调节此参数进行调优） -->
        //此属性如果设置为true，为用shiro默认的30min缓存，退出或者修改角色权限导致
        //的删除缓存不生效 fht 2020-02-28
        shiroRealm.setAuthenticationCachingEnabled(false);
        return shiroRealm;
    }


    @Bean("sessionManager")
    public DefaultWebSessionManager sessionManager(RedisSessionDAO sessionDAO, SimpleCookie sessionIdCookie) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //session存活时间60分钟
        sessionManager.setGlobalSessionTimeout(3600000);
        sessionManager.setDeleteInvalidSessions(true);
        //自定义监听 fht 不能使用@WebListern的 HttpSessionListerner 因为shiro重写了session 2020-03-05
        Collection<SessionListener> sessionListeners = new ArrayList<>();
        sessionListeners.add(sessionListener());
        sessionManager.setSessionListeners(sessionListeners);
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
        shiroFilterFactoryBean.setLoginUrl("/unauthorized");
        //shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
        //shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
        //shiroFilterFactoryBean.setLoginUrl(unauthorizedUrl);
        shiroFilterFactoryBean.setSuccessUrl(homepageUrl);
       // shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

//        Map<String, Filter> filters = new HashMap<>();
//        //filters.put("casFilter", casFilter);
//        shiroFilterFactoryBean.setFilters(filters);
        //注意此处使用的是LinkedHashMap，是有顺序的，shiro会按从上到下的顺序匹配验证，匹配了就不再继续验证
        //所以上面的url要苛刻，宽松的url要放在下面，尤其是"/**"要放到最下面，如果放前面的话其后的验证规则就没作用了。
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/nginx.html", "anon");
        filterChainDefinitionMap.put("/common/filesUpload", "anon");
        filterChainDefinitionMap.put("/common/upload", "anon");
        filterChainDefinitionMap.put("/loginoutController/*","anon");
        filterChainDefinitionMap.put("/authManageController/*","anon");
        filterChainDefinitionMap.put("/dispatcher/changeStatus", "anon");
        filterChainDefinitionMap.put("/driver/getUnderWayDriver", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/updateLevel", "anon");
        filterChainDefinitionMap.put("/permission/levelList", "anon");
        filterChainDefinitionMap.put("/car/batchImport", "anon");
        //注意此处：如果是h5放开的话 会出现跨域问题
        filterChainDefinitionMap.put("/unauthorized", "anon");
        filterChainDefinitionMap.put("/getMsgCode", "anon");
        filterChainDefinitionMap.put("/dologin", "anon");
        filterChainDefinitionMap.put("/dologout", "anon");
        filterChainDefinitionMap.put("/logout.html", "logout");
        filterChainDefinitionMap.put("/**", "user");
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 开启shiro注解
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor shiroPermisson(DefaultWebSecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * 自定义shiro监听
     * @return
     */
    @Bean("sessionListener")
    public LoginoutListener sessionListener(){
        LoginoutListener loginoutListener = new LoginoutListener();

        return loginoutListener;
    }

}

