package com.sq.transportmanage.gateway.api.web.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sq.transportmanage.gateway.api.common.Constants;
import com.sq.transportmanage.gateway.api.util.IPv4Util2;
import com.sq.transportmanage.gateway.dao.entity.driverspark.BaseMerchant;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.base.BaseMerchantMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SupplierExtMapper;
import com.sq.transportmanage.gateway.service.auth.DataPermissionService;
import com.sq.transportmanage.gateway.service.common.enums.DataLevelEnum;
import com.sq.transportmanage.gateway.service.common.shiro.realm.SSOLoginUser;
import com.sq.transportmanage.gateway.service.common.shiro.session.WebSessionUtil;
import mp.mvc.logger.entity.LoggerDto;
import mp.mvc.logger.message.MpLoggerMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
    private BaseMerchantMapper baseMerchantMapper;

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
        if(request.getRequestURL().toString().contains(Constants.URL)){
            return ctx;
        }
        SSOLoginUser loginUser = WebSessionUtil.getCurrentLoginUser();
        logger.info(String.format("%s loginUser %s", request.getMethod(), loginUser.getName()));
        /**?????????????????????**/
         boolean bl = true;
        if(bl) {
            if (loginUser != null) {
                JSONObject data = new JSONObject();
                data.put("sysId", "t_saas");
                data.put("id", loginUser.getId());
                data.put("merchantId", loginUser.getMerchantId() + "");
                data.put("account", loginUser.getLoginName());
                BaseMerchant baseMerchant = baseMerchantMapper.selectByPrimaryKey(loginUser.getMerchantId());
                if (baseMerchant != null) {
                    String decodeStr = "";
                    String merchantNameStr = "";
                    try {
                        decodeStr = URLEncoder.encode(loginUser.getName(), "UTF-8");
                        merchantNameStr = URLEncoder.encode(baseMerchant.getMerchantName(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    /**???????????????*/
                    data.put("name", decodeStr);
                    data.put("merchantName", merchantNameStr);
                }
                /**?????????????????????*/
                data.put("supplierIds", loginUser.getSupplierIds());
                /**?????????????????????*/
                data.put("cityIds", loginUser.getCityIds());
                /**??????????????????*/
                data.put("teamIds", loginUser.getTeamIds());
                /**??????????????????*/
                data.put("groupIds", loginUser.getGroupIds());
                /**??????????????????*/
                data.put("dataLevel", loginUser.getDataLevel());
                /**????????????????????????*/
                data.put("accountType",loginUser.getAccountType());
                logger.info("LOGINUSER :{}", data);
                ctx.addZuulRequestHeader("LOGINUSER", data.toJSONString());
            } else {
                /**???????????????????????????????????????*/
                ctx.setSendZuulResponse(false);
                /**???????????????*/
                ctx.setResponseStatusCode(401);
                /**??????????????????*/
                ctx.setResponseBody("{\"code\":0,\"result\":\"??????????????????!????????????\"}");
                ctx.set("isSuccess", false);
            }

            /**??????????????????*/
            if (loginUser != null && StringUtils.isNotBlank(loginUser.getLoginName()) && loginUser.getId() != null) {
                String tracId = MDC.get("reqId");
                if (StringUtils.isBlank(tracId)) {
                    tracId = request.getHeader("x_requestId");
                }
                /**????????????*/
                LoggerDto dto = this.getBuiness(request, tracId, loginUser, request.getSession().getId());
                MpLoggerMessage.sendLoggerMessage(dto, request);

            }

        }
        return ctx;
    }

    private LoggerDto getBuiness(HttpServletRequest request , String traceId, SSOLoginUser loginUser, String sessionId) {
        LoggerDto dto = new LoggerDto();
        dto.setCreateTime(System.currentTimeMillis());
        dto.setSessionId(StringUtils.isNotBlank(sessionId) ? sessionId : "");
        dto.setUserAccount(StringUtils.isNotBlank(loginUser.getLoginName()) ? loginUser.getLoginName() : null);
        dto.setUserIp(IPv4Util2.getClientIpAddr(request));
        dto.setUserId(String.valueOf(loginUser.getId()));
        dto.setRemark(StringUtils.isNotBlank(loginUser.getName()) ? loginUser.getName() : null);
        dto.setTraceId(StringUtils.isNotBlank(traceId) ? traceId : "");
        return dto;
    }
}