package com.sq.transportmanage.gateway.service.common.shiro.filter;

import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author:admin
 * @Date:2019/9/6  如果是ajax请求，header头部直接返回403
 * @Description:
 */
public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {

    @Value(value = "${homepage.url}")
    private String homePageUrl;

    public ShiroFormAuthenticationFilter() {
        super();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        if(isLoginRequest(request,response)){
            return super.onAccessDenied(request,response);
        }else {
            if(isAjax((HttpServletRequest) request)){
                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                httpServletResponse.addHeader("REQUIRE_AUTH","true");
                httpServletResponse.setStatus(HttpStatus.SC_FORBIDDEN);
            }else {
                super.saveRequestAndRedirectToLogin(request,response);
            }
        }
        return false;
    }


    /**
     * 是否是ajax请求
     * @param request
     * @return
     */
    private boolean isAjax(HttpServletRequest request){
        String requestHeader = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestHeader);
    }



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
