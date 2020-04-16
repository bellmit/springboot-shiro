package com.sq.transportmanage.gateway.api.web.filter;

import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author fanht
 * @Description session失效后跨域问题
 * @Date 2020/3/25 上午2:23
 * @Version 1.0
 */
@Component
public class InterFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {


        HttpServletRequest httpRequest = (HttpServletRequest) request;

        HttpServletResponse res = (HttpServletResponse) response;


        res.setContentType("text/html;charset=UTF-8");

        res.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));

        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");

        res.setHeader("Access-Control-Max-Age", "2592000");

        res.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");

        res.setHeader("Access-Control-Allow-Credentials", "true");

        res.setHeader("XDomainRequestAllowed","1");

        res.setHeader("XDomainRequestAllowed","1");

        chain.doFilter(request, response);

    }
}
