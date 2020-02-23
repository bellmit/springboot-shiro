//package com.sq.mp.carmanage.api.interceptor;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * @author (yangbo)
// * @Date: 2019/5/8 17:18
// * @Description:(记录访问日志)
// */
//public class BaseLogInterceptors extends HandlerInterceptorAdapter {
//
//    private static final Logger logger = LoggerFactory.getLogger(BaseLogInterceptors.class);
//
//    private final static String TRACE_KEY = "traceId";
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String traceId = UUID.randomUUID().toString().replace("-", "");
//        MDC.put(TRACE_KEY, traceId);
//        Map<String, String> params = ServletUtils.getParameters(request);
//        logger.info(request.getRequestURI() + (StringUtils.isBlank(request.getQueryString())? "" :"?"+ request.getQueryString())
//                + " params:" + params.toString());
//        return super.preHandle(request, response, handler);
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        MDC.remove(TRACE_KEY);
//        super.afterCompletion(request, response, handler, ex);
//    }
//}
