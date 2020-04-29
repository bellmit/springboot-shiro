package com.sq.transportmanage.gateway.api.web.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sq.transportmanage.gateway.dao.entity.driverspark.BaseMerchant;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.base.BaseMerchantMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.BaseMerchantCityConfigExMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.SupplierExtMapper;
import com.sq.transportmanage.gateway.service.auth.DataPermissionService;
import com.sq.transportmanage.gateway.service.base.BaseDriverTeamService;
import com.sq.transportmanage.gateway.service.common.enums.DataLevelEnum;
import com.sq.transportmanage.gateway.service.common.shiro.realm.SSOLoginUser;
import com.sq.transportmanage.gateway.service.common.shiro.session.WebSessionUtil;
import com.sq.transportmanage.gateway.service.vo.BaseMerchantCityConfigVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
    private BaseMerchantMapper baseMerchantMapper;

    @Autowired
    private SupplierExtMapper supplierExtMapper;

    @Autowired
    private DataPermissionService dataPermissionService;

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
            data.put("id",loginUser.getId());//商户ID
            data.put("merchantId",loginUser.getMerchantId()+"");//商户ID
            data.put("account",loginUser.getLoginName());//用户名
            BaseMerchant baseMerchant = baseMerchantMapper.selectByPrimaryKey(loginUser.getMerchantId());
            if(baseMerchant != null){
                String decodeStr = "";
                String merchantNameStr = "";
                try {
                    decodeStr = URLEncoder.encode(loginUser.getName(),"UTF-8");
                    merchantNameStr = URLEncoder.encode(baseMerchant.getMerchantName(),"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                data.put("name", decodeStr);//用户名中文
                data.put("merchantName", merchantNameStr);//用户名中文
            }

            //TODO  等1.3上线以后放到登录成功设置值的时候
            dataPermissionService.populateLoginUser(loginUser);
            data.put("supplierIds",loginUser.getSupplierIds());//运力商数据权限
            data.put("cityIds",loginUser.getCityIds());//城市商数据权限
            data.put("teamIds",loginUser.getTeamIds());//车队数据权限
            data.put("groupIds",loginUser.getGroupIds());//班组数据权限
            data.put("dataLevel",loginUser.getDataLevel());//数据权限级别
            logger.info("LOGINUSER :{}",data);
            ctx.addZuulRequestHeader("LOGINUSER",data.toJSONString());
        }else{
            ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
            ctx.setResponseStatusCode(401);// 返回错误码
            ctx.setResponseBody("{\"code\":0,\"result\":\"网关验证失败!请先登录\"}");// 返回错误内容
            ctx.set("isSuccess", false);
        }
        return ctx;
    }

}