package com.sq.transportmanage.gateway.api.web.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author fanht
 * @Description 解决shiro跳转地址  http变更为https
 * @Date 2020/3/11 下午8:34
 * @Version 1.0
 */
@Component
public class AbsoluteSendRedirectFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpTransWrapper transWrapper = new HttpTransWrapper(request,response);
        filterChain.doFilter(request,transWrapper);
    }
}
