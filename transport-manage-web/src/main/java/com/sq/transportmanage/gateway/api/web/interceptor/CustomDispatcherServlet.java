package com.sq.transportmanage.gateway.api.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.sq.transportmanage.gateway.api.common.ResponseResult;
import com.sq.transportmanage.gateway.service.common.web.RestErrorCode;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 实现统一处理404的问题
 **/
public class CustomDispatcherServlet extends DispatcherServlet {
    private static final Logger log = LoggerFactory.getLogger(CustomDispatcherServlet.class);
    private static final long serialVersionUID = 1000001000000000001L;

    @Override
    protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.error("请求资源不存在，URI= " + request.getRequestURI());
        PrintWriter out = null;
        try {
            response.setStatus(HttpStatus.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
            response.setHeader("pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            ResponseResult responseResult = ResponseResult.fail(RestErrorCode.HTTP_NOT_FOUND);
            responseResult.setMsg("HTTP_STATUS_404：请求资源不存在！");
            out = response.getWriter();
            out.write(JSON.toJSONString(responseResult, true));
            out.close();
        } catch (Exception e) {
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}