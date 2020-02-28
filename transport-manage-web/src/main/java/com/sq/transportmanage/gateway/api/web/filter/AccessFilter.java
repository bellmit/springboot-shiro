package com.sq.transportmanage.gateway.api.web.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sq.transportmanage.gateway.api.common.AuthEnum;
import com.sq.transportmanage.gateway.service.shiro.realm.SSOLoginUser;
import com.sq.transportmanage.gateway.service.shiro.session.WebSessionUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: sq-union-manage
 * @description: AccessFilter
 * @author: zjw
 * @create: 2020-02-23 18:57
 **/
@Component
public class AccessFilter extends ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        logger.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        SSOLoginUser loginUser = WebSessionUtil.getCurrentLoginUser();
        logger.info(String.format("%s loginUser %s", loginUser.getName()));
        /**用户是否有权限**/
        boolean bl = false;
        //如果是管理员 直接通过
        if(AuthEnum.MANAGE.getAuthId().equals(loginUser.getAccountType())){
            bl = true;
        }else {
            String uri = request.getRequestURI().toString();
            List<String> menuUrl = loginUser.getMenuUrlList();
            if(menuUrl.contains(uri)){
                bl = true;
            }
        }
        if(bl){
            ctx.addZuulRequestHeader("user_token",JSONObject.toJSONString(loginUser));
        }else{
            ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
            ctx.setResponseStatusCode(401);// 返回错误码
            ctx.setResponseBody("{\"code\":0,\"result\":\"网关验证失败!验证方式为2\"}");// 返回错误内容
            ctx.set("isSuccess", false);
        }
        return ctx;
    }

}