package com.sq.transportmanage.gateway.api.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/***
 * @author admin
 * 全局traceId追踪
 */
@WebFilter(urlPatterns = "/*", filterName = "traceIdFilter")
public class TraceIdFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TraceIdFilter.class);

    /**
     * log 追踪ID
     */
    private final static String TRACE_KEY = "traceId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //先从param里取，没有的话从header里取，还没有的话再创建
        String traceId = request.getParameter("TRACE_ID");
        if (StringUtils.isBlank(traceId)){
            traceId = httpServletRequest.getHeader("X-Request-Id");
        }

        if (StringUtils.isBlank(traceId)){
            traceId = UUID.randomUUID().toString().replace("-", "");
            logger.debug("生成traceId:{}", traceId);
        }

        MDC.put(TRACE_KEY, traceId);

        chain.doFilter(request, response);

        MDC.remove(TRACE_KEY);

    }

    @Override
    public void destroy() {

    }
}