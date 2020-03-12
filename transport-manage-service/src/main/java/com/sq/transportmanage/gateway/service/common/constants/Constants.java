package com.sq.transportmanage.gateway.service.common.constants;

/**
 * 系统常量
 *
 * @author answer
 * @Date: 2019/8/28 09:53
 */
public final class Constants {

    public static final Integer ERROR = 1;
    public static final Integer SUCCESS = 0;

    /**
     * {@code 500 Server Error} (HTTP/1.0 - RFC 1945)
     */
    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /**
     * {@code 200 OK} (HTTP/1.0 - RFC 1945)
     */
    public static final Integer SC_OK_200 = 200;

    /**
     * 访问权限认证未通过 510
     */
    public static final Integer SC_JEECG_NO_AUTHZ = 510;


    /**
     * 管理员MD5机密
     */
    public static String MANAGE_MD5 = "YujxVDA8w4uA5c9eIGv/QA==";

    /**通过邮箱找回密码key**/
    public static final String RESET_EMAIL_KEY = "star_fire_reset_password_by_email_";

    /**通过邮箱找回密码code**/
    public static final String RESET_EMAIL_CODE = "star_fire_reset_password_by_code_";


    /**通过手机找回密码key**/
    public static final String RESET_PHONE_KEY = "star_fire_reset_password_by_phone_";


    /**邮箱**/
    public static final String EMAIL = "1";

    /**手机**/
    public static final String PHONE = "2";


}
