/*
package com.sq.transportmanage.gateway.api.web.interceptor;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

*/
/**
 * 验证参数合法性拦截器
 *
 * @author gengjingwan
 *//*

public class CharacterValidInterceptors extends HandlerInterceptorAdapter {

    private static final Log log = LogFactory.getLog(CharacterValidInterceptors.class);

    */
/**
     * 执行方法前
     *//*

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        //log.info("start BaseValidInterceptors");
        Map<String, String> param = ServletUtils.getParameters(request);
//        log.info("请求参数 :" + param);
        String _r = CharacterFilter.validScriptFilter(param);
        if (_r != null) {
            Map json = new HashMap();
            json.put("status", "0");
            json.put("message", "抱歉，您的请求参数[" + _r + "]输入不合法");
            json.put("result", null);
            log.error("请求参数含有SCRIPT注入");
            response.getWriter().write(JSON.toJSONString(json));
            return false;
        }
        return super.preHandle(request, response, handler);
    }

}
*/
