package com.sq.transportmanage.gateway.api.auth;

import com.alibaba.fastjson.JSONObject;
import com.sq.transportmanage.gateway.api.web.interceptor.AjaxResponse;
import com.sq.transportmanage.gateway.api.web.interceptor.LoginoutListener;
import com.sq.transportmanage.gateway.service.common.shiro.session.RedisSessionDAO;
import com.sq.transportmanage.gateway.service.common.web.RestErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author fanht
 * @Description
 * @Date 2020/3/5 下午6:46
 * @Version 1.0
 */
@RestController
@RequestMapping("/loginoutController")
public class LoginoutController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private RedisSessionDAO redisSessionDAO;

    @RequestMapping("/userLoginOut")
    @ResponseBody
    public AjaxResponse userLoginOut(String userIds, HttpSession httpSession,
                                     HttpServletRequest request){

        logger.info("httpSessionId" + httpSession.getId() + ",是否是session会话：" +
        request.getSession(false));
        HttpSession session = request.getSession();
        logger.info("获取监听存取的信息" + JSONObject.toJSONString(LoginoutListener.sessionCount));
        try {
            String userId[] = StringUtils.tokenizeToStringArray(userIds,",");
            List<Integer> userList = new ArrayList<Integer>();
            for(int i = 0;i<userId.length;i++){
                redisSessionDAO.clearRelativeSession(null,null,Integer.valueOf(userId[i]));
             }

            return AjaxResponse.success(null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            logger.error("shiro退出异常" + e);
            return AjaxResponse.fail(RestErrorCode.UNKNOWN_ERROR);
        }
    }

    /*@Override
    public void clearRelativeSession(Integer permissionId, Integer roleId, Integer userId) {
        super.clearRelativeSession(null, null, userId);
    }*/
}
