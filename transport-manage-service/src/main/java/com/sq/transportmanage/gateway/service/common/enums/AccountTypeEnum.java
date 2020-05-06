package com.sq.transportmanage.gateway.service.common.enums;

/***
* @Description: 账户类型
* @Author: zjw
* @Date: 2020/4/29
*/
public enum AccountTypeEnum {

//    账户类型 100普通用户  900 商户管理员  1000超级管理员
    NORMAL_USER(100,"普通用户"),
    MERCHANT_ADM(900,"商户管理员"),
    SUPER_ADM(1000,"超级管理员");

    private Integer code;

    private String msg;

    AccountTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
