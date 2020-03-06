package com.sq.transportmanage.gateway.api.auth;


import com.sq.transportmanage.gateway.service.auth.AuthManageService;
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
public class AuthManageController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthManageService authManageService;

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
     * @param email
     * @return
     * @throws MessagingException
     */
    @ResponseBody
    @RequestMapping("/isExpire")
    public AjaxResponse isExpire(@Verify(param = "email",rule = "required")String email) {
        logger.info("判断是否过期入参：" + email);
        return authManageService.isExpire(email);
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
                                @Verify(param = "newPassword",rule = "required")String newPassword){
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