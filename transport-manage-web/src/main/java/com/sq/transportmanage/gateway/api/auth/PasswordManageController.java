package com.sq.transportmanage.gateway.api.auth;


import com.sq.transportmanage.gateway.service.auth.PasswordManageService;
import com.sq.transportmanage.gateway.service.common.web.AjaxResponse;
import com.sq.transportmanage.gateway.service.common.web.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.text.MessageFormat;

/**
 * @Author fanht
 * @Description
 * @Date 2020/3/3 下午9:15
 * @Version 1.0
 */
@RestController
@RequestMapping("/authManageController")
public class PasswordManageController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PasswordManageService authManageService;

    /**
     * 根据邮箱找回密码
     * @param email
     * @return
     * @throws MessagingException
     */
    @ResponseBody
    @RequestMapping("/toFindPassword")
    public AjaxResponse toFindPassword(@Verify(param = "email",rule = "required")String email) throws MessagingException {
        logger.info("通过邮箱找回密码功能入参：" + email);

        return authManageService.sendMail(email);
    }

    /**
     * 是否过期
     * @param type 1 邮箱 2 短信
     * @param
     * @return
     * @throws MessagingException
     */
    @ResponseBody
    @RequestMapping("/isExpire")
    public AjaxResponse isExpire(@Verify(param = "type",rule = "required") String type,
                                 @Verify(param = "param",rule = "required")String param) {
        logger.info("判断是否过期入参：type:" + type + ",param:" + param);
        return authManageService.isExpire(type,param);
    }


    /**
     * 重置密码
     * @param email
     * @param newPassword
     * @return
     */
    @ResponseBody
    @RequestMapping("/resetPassword")
    public AjaxResponse resetPassword(@Verify(param = "email",rule = "required")String email,
                                @Verify(param = "newPassword",rule = "required|resetPassword(^[a-zA-Z0-9_\\-]{8,50}$)")String newPassword){
        logger.info(MessageFormat.format("重置密码入参：email:{0},newPassword:{1}"
                ,email,newPassword));
        return authManageService.resetPassword(email,newPassword);
    }


    /**
     * 获取验证码
     * @param phone
     * @return
     * @throws MessagingException
     */
    @ResponseBody
    @RequestMapping("/getMsgCode")
    public AjaxResponse getMsgCode(@Verify(param = "phone",rule = "required")String phone) {
        logger.info("获取验证码：" + phone);

        return authManageService.getMsgCode(phone);
    }

    /**
     * 验证验证码是否正确
     * @param phone
     * @return
     * @throws MessagingException
     */
    @ResponseBody
    @RequestMapping("/msgCodeVerify")
    public AjaxResponse msgCodeVerify(@Verify(param = "phone",rule = "required")String phone,
                                @Verify(param = "msgCode",rule = "required")String msgCode) {
        logger.info("验证验证码：" + phone + ",msgCode:" + msgCode);

        return authManageService.msgCodeVerify(phone,msgCode);
    }

    /**
     * 验证验证码是否正确
     * @param phone
     * @return
     * @throws MessagingException
     */
    @ResponseBody
    @RequestMapping("/resetPasswordByPhone")
    public AjaxResponse resetPasswordByPhone(@Verify(param = "phone",rule = "required")String phone,
                                @Verify(param = "newPassword",rule = "required")String newPassword) {
        logger.info("验证验证码：" + phone + ",newPassword:" + newPassword);

        return authManageService.resetPasswordByPhone(phone,newPassword);
    }

}