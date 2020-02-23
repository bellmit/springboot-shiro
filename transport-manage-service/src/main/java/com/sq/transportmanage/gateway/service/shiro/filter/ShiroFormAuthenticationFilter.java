package com.sq.transportmanage.gateway.service.shiro.filter;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author:qxx
 * @Date:2019/9/6
 * @Description:
 */
public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {
    @Value(value = "${homepage.url}")
    private String homePageUrl;

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse rep = toHttp(response);
        rep.setStatus(302);
        rep.setHeader("Location", homePageUrl);
        return false;
    }


    public  HttpServletResponse toHttp(ServletResponse response) {
        return (HttpServletResponse)response;
    }


}
