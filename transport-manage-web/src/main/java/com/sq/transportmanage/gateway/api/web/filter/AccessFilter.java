package com.sq.transportmanage.gateway.api.web.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sq.transportmanage.gateway.api.util.IPv4Util2;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SupplierExtMapper;
import com.sq.transportmanage.gateway.service.common.shiro.realm.SSOLoginUser;
import com.sq.transportmanage.gateway.service.common.shiro.session.RedisSessionDAO;
import com.sq.transportmanage.gateway.service.common.shiro.session.WebSessionUtil;
import mp.mvc.logger.entity.LoggerDto;
import mp.mvc.logger.message.MpLoggerMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    @Autowired
    private SupplierExtMapper supplierExtMapper;

    @Resource(name = "sessionDAO")
    private RedisSessionDAO redisSessionDAO;


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
        if(request.getRequestURL().toString().contains("common/upload")){
            return ctx;
        }
        SSOLoginUser loginUser = WebSessionUtil.getCurrentLoginUser();
        logger.info(String.format("%s loginUser %s", request.getMethod(), loginUser.getName()));
        /**用户是否有权限**/
        boolean bl = true;
        //如果是管理员 直接通过
        /*if(AuthEnum.MANAGE.getAuthId().equals(loginUser.getAccountType())){
            bl = true;
        }else {
            String uri = request.getRequestURI().toString();
            List<String> menuUrl = loginUser.getMenuUrlList();
            if(menuUrl.contains(uri)){
                bl = true;
            }
        }*/
        if(bl){
            JSONObject data = new JSONObject();
            data.put("sysId","t_saas");//平台ID
            data.put("merchantId",loginUser.getMerchantId()+"");//商户ID
            data.put("account",loginUser.getLoginName());//用户名
            String decodeStr = "";
            try {
                decodeStr = URLEncoder.encode(loginUser.getName(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            data.put("name", decodeStr);//用户名中文
            if(1==loginUser.getLevel()){
                List<Integer> supplierIds = supplierExtMapper.selectListByMerchantId(loginUser.getMerchantId());
                if(!CollectionUtils.isEmpty(supplierIds)){
                    data.put("supplierIds", StringUtils.join(supplierIds.toArray(), ","));
                }
            }else{
                data.put("supplierIds",loginUser.getSupplierIds());//运力商数据权限
            }
            logger.info("LOGINUSER :{}",data);
            ctx.addZuulRequestHeader("LOGINUSER",data.toJSONString());
        }else{
            ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
            ctx.setResponseStatusCode(401);// 返回错误码
            ctx.setResponseBody("{\"code\":0,\"result\":\"网关验证失败!请先登录\"}");// 返回错误内容
            ctx.set("isSuccess", false);
        }

//            JSONObject data = new JSONObject();
//            data.put("sysId","t_saas");//平台ID
//            data.put("merchantId",);//商户ID
//            data.put("account","admin");//用户名
//            String decodeStr = "";
//            try {
//                decodeStr = URLDecoder.decode("默认超级管理员","UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            data.put("name", decodeStr);//用户名中文
//            data.put("supplierIds","1,2,3,5,6,7,8,9,10,11,12,13,15,16,18,20,21,22,24,25,26");//用户名中文
//            logger.info("login_user :{}",data);
//            ctx.addZuulRequestHeader("LOGINUSER",data.toJSONString());
//            ctx.addZuulRequestHeader("loginuser",data.toJSONString());

        String traceId = request.getParameter("TRACE_ID");
        if (StringUtils.isBlank(traceId)){
            traceId = request.getHeader("X-Request-Id");
        }
        //增加用户行为
        if(loginUser != null && StringUtils.isNotBlank(loginUser.getLoginName()) && loginUser.getId() != null){
            //发送消息
            LoggerDto dto = this.getBuiness(request,traceId,loginUser);
            MpLoggerMessage.sendLoggerMessage(dto,request);
        }

        return ctx;
    }

    private LoggerDto getBuiness(HttpServletRequest request , String traceId,  SSOLoginUser ssoLoginUser) {
        LoggerDto dto = new LoggerDto();
        dto.setCreateTime(System.currentTimeMillis());
        Subject currentLoginUser = SecurityUtils.getSubject();
        String sessionId =  (String)currentLoginUser.getSession().getId() ;
        dto.setSessionId(StringUtils.isNotBlank(sessionId) ? sessionId : "");
        dto.setUserAccount(StringUtils.isNotBlank(ssoLoginUser.getLoginName()) ? ssoLoginUser.getLoginName() : null);
        dto.setUserIp(IPv4Util2.getClientIpAddr(request));
        dto.setUserId(String.valueOf(ssoLoginUser.getId()));
        dto.setRemark(StringUtils.isNotBlank(ssoLoginUser.getName()) ? ssoLoginUser.getName() : null);
        dto.setTraceId(StringUtils.isNotBlank(traceId) ? traceId : "");
        return dto;
    }
}