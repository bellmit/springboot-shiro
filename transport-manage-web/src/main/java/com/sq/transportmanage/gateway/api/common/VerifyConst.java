package com.sq.transportmanage.gateway.api.common;

/**
 * 常量定义
 **/
public final class VerifyConst {
    /**
     * 手机号码验证正则表达式
     **/
    public static final String MOBILE_REGEX = "^1(3|4|5|6|7|8|9)[0-9]{9}$";
    /**
     * 账号的正则表达式
     **/
    public static final String ACCOUNT_REGEX = "^[a-zA-Z0-9_\\-]{3,30}$";
    /**
     * 电子邮箱的正则表达式
     **/
    public static final String EMAIL_REGEX = "^[A-Za-z0-9]+([-_\\.][A-Za-z0-9]+)*@([-A-Za-z0-9]+[\\.])+[A-Za-z0-9]+$";

}
