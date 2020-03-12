package com.sq.transportmanage.gateway.service.auth;

import com.sq.transportmanage.gateway.dao.entity.driverspark.CarAdmUser;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.CarAdmUserMapper;
import com.sq.transportmanage.gateway.dao.mapper.driverspark.ex.CarAdmUserExMapper;
import com.sq.transportmanage.gateway.service.common.cache.RedisUtil;
import com.sq.transportmanage.gateway.service.common.constants.Constants;
import com.sq.transportmanage.gateway.service.common.shiro.cache.RedisCache;
import com.sq.transportmanage.gateway.service.common.web.AjaxResponse;
import com.sq.transportmanage.gateway.service.common.web.RestErrorCode;
import com.sq.transportmanage.gateway.service.util.DateUtil;
import com.sq.transportmanage.gateway.service.util.NumberUtil;
import com.sq.transportmanage.gateway.service.util.PasswordUtil;
import com.sq.transportmanage.gateway.service.util.SmsSendUtil;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Random;

/**
 * @Author fanht
 * @Description 密码找回相关操作
 * @Date 2020/3/3 下午9:20
 * @Version 1.0
 */
@Service
public class PasswordManageService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    @Value("${spring.mail.username}")
    private String mainUserName;

    @Autowired
    private CarAdmUserExMapper carAdmUserExMapper;

    @Autowired
    private CarAdmUserMapper carAdmUserMapper;

    @Autowired
    private JavaMailSender javaMailSender;



    @Autowired
    private RedisUtil redisUtil;


    @Value("${reset.password.url}")
    private String resetPasswordUrl;





    /**
     * 发送邮件
     * @param email
     * @return
     * @throws MessagingException
     */
    public AjaxResponse sendMail(String email) throws MessagingException {
        CarAdmUser carAdmUser = carAdmUserExMapper.queryExist(email,null);
        if(carAdmUser == null){
            logger.info("======邮箱不存在=====");
            return AjaxResponse.fail(RestErrorCode.EMAIL_UNEXIST);
        }
        //生成验证码
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        // 5分钟后过期

        redisUtil.set(Constants.RESET_EMAIL_KEY + email,verifyCode,5*60);


        //将验证码 和 过期时间更新到数据库

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html><head><title>首汽约车——用户密码找回</title></head><body>");
        stringBuilder.append("尊敬的").append(carAdmUser.getAccount()).append("<br/>");
        stringBuilder.append("您在"+ DateUtil.getMailTimeString(new Date())+"提交找回密码请求,请点击下面的连接修改用户密码").append("<br/>");
        stringBuilder.append("/br");
        stringBuilder.append(""+resetPasswordUrl+"").append("?").append(email).append("<br/>");
        stringBuilder.append("(如果您无法点击这个链接,请将次连接复制到浏览器地址栏后访问)<br/>");
        stringBuilder.append("为了保证您账号的安全性，该连接有效期为24小时，并且点击一次后将失效!<br/>");
        stringBuilder.append("设置并牢记密码保护问题将更好的保障您的账号安全。<br/>");
        stringBuilder.append("如果您误收到此电子邮件，则可能是其他用户在尝试账号设置的误操作，如果您未发起该请求，则无需再进行任<br/>");
        stringBuilder.append("何操作，并可以放心的忽略此电子邮件。<br/>");

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setFrom(mainUserName);//这里只是设置username 并没有设置host和password，因为host和password在springboot启动创建JavaMailSender实例的时候已经读取了
        mimeMessageHelper.setTo(email);
        mimeMessage.setSubject("首汽约车——用户密码找回");
        mimeMessageHelper.setText(stringBuilder.toString(),true);
        javaMailSender.send(mimeMessage);

        logger.info("======邮件发送成功end========");
        return AjaxResponse.success(null);
    }



    /**
     * 判断是否过期
     * @return
     */
    public AjaxResponse isExpire(String type,String param){
        logger.info("===判断是否过期===" );

        if(!Constants.EMAIL.equals(type) && !Constants.PHONE.equals(type) ) {
            logger.info("======通过密码找回类型不匹配=====");
            return AjaxResponse.fail(RestErrorCode.RESET_TYPE_UNEXIST);
        }

        CarAdmUser carAdmUser = null;
        //邮箱验证以及邮箱验证码验证
        if(Constants.EMAIL.equals(type)){
            carAdmUser = carAdmUserExMapper.queryExist(param,null);
            if(carAdmUser == null){
                logger.info("======邮箱不存在=====");
                return AjaxResponse.fail(RestErrorCode.EMAIL_UNEXIST);
            }

            if(!redisUtil.hasKey(Constants.RESET_EMAIL_KEY+param)){
                logger.info("======邮箱验证码已过期=====");
                return AjaxResponse.fail(RestErrorCode.EMAIL_VERIFY_EXPIRED);
            }

        }

        //手机验证以及手机验证码验证
        if(Constants.PHONE.equals(type)){
            carAdmUser = carAdmUserExMapper.queryExist(null,param);
            if(carAdmUser == null){
                logger.info("======根据手机查询账号不存在=====");
                return AjaxResponse.fail(RestErrorCode.PHONE_NOT_EXIST);
            }

            if(!redisUtil.hasKey(Constants.RESET_PHONE_KEY+param)){
                logger.info("======手机验证码已过期=====");
                return AjaxResponse.fail(RestErrorCode.PHONE_CODE_EXPIRE);
            }

        }




        return AjaxResponse.success(null);
    }


    /**
     * 重置密码
     * @return
     */
    public AjaxResponse resetPassword(String email,String newPassword){
        logger.info("====重置密码start=======入参：newPassword:" + newPassword +",email:" +email);
        CarAdmUser carAdmUser = carAdmUserExMapper.queryExist(email,null);
        if(carAdmUser == null){
            logger.info("======邮箱不存在=====");
            return AjaxResponse.fail(RestErrorCode.EMAIL_UNEXIST);
        }

        if(!redisUtil.hasKey(Constants.RESET_EMAIL_KEY+email)){
            logger.info("======邮箱验证码已过期=====");
            return AjaxResponse.fail(RestErrorCode.EMAIL_VERIFY_EXPIRED);
        }


        String md5Password = PasswordUtil.md5(newPassword, carAdmUser.getAccount());

        carAdmUser.setPassword(md5Password);
        carAdmUser.setUpdateDate(new Date());
        carAdmUser.setUpdateUser(carAdmUser.getUserName());

        int upCode = carAdmUserMapper.updateByPrimaryKey(carAdmUser);

        if(upCode > 0){
            logger.info("=======更改密码成功end========");
            redisUtil.delete(email);
            return AjaxResponse.success(null);
        }else {
            return AjaxResponse.fail(RestErrorCode.UNKNOWN_ERROR);
        }

    }


    public AjaxResponse getMsgCode(String phone){

        CarAdmUser carAdmUser = carAdmUserExMapper.queryExist(null,phone);

        if(carAdmUser == null){
            logger.info("======手机号码不存在=====");
            return AjaxResponse.fail(RestErrorCode.PHONE_NOT_EXIST);
        }

        String key = Constants.RESET_PHONE_KEY + phone;


        String  msgcode = redisUtil.get(key);
        if(msgcode == null){
            msgcode = NumberUtil.genRandomCode(8);
        }

        redisUtil.set(key,msgcode,2*60*1000);
        String content = "尊敬的用户，您重置密码验证码为" + msgcode + ",有效期120秒";

        SmsSendUtil.send(phone,content);


        logger.info("=======短信发送成功,验证码===" + content);
        return AjaxResponse.success(null);
    }

    /**
     * 判断验证码是否正确
     * @return
     */
    public AjaxResponse msgCodeVerify(String phone,String msgCode){

        logger.info("====判断验证码是否正确=======入参：phone:" + phone +",msgCode:" +msgCode);


        String key = Constants.RESET_PHONE_KEY + phone;

        if(!redisUtil.hasKey(key)){
            logger.info("======验证码已过期=====");
            return AjaxResponse.fail(RestErrorCode.PHONE_CODE_EXPIRE);
        }

        String  cacheMsgCode = redisUtil.get(key) == null ? "" : redisUtil.get(key).toString().trim();

        logger.info("=======获取msgCode=====" + cacheMsgCode);
        if(msgCode.equals(cacheMsgCode)){
            logger.info("=======手机验证码正常========");
            return AjaxResponse.success(null);
        }else {
            return AjaxResponse.fail(RestErrorCode.PHONE_CODE_UNSPECIAL);
        }

    }



    /**
     * 根据手机号重置密码
     * @return
     */
    public AjaxResponse resetPasswordByPhone(String phone,String newPassword){
        logger.info("====手机号码重置密码start=======入参：newPassword:" + newPassword +",phone:" +phone);
        CarAdmUser carAdmUser = carAdmUserExMapper.queryExist(null,phone);
        if(carAdmUser == null){
            logger.info("======手机号码不存在=====");
            return AjaxResponse.fail(RestErrorCode.EMAIL_UNEXIST);
        }


        String md5Password = PasswordUtil.md5(newPassword, carAdmUser.getAccount());

        carAdmUser.setPassword(md5Password);

        carAdmUser.setUpdateDate(new Date());
        carAdmUser.setUpdateUser(carAdmUser.getUserName());

        int upCode = carAdmUserMapper.updateByPrimaryKey(carAdmUser);

        if(upCode > 0){
            logger.info("=======更改密码成功end========");
            return AjaxResponse.success(null);
        }else {
            return AjaxResponse.fail(RestErrorCode.UNKNOWN_ERROR);
        }

    }


}
