package com.sq.transportmanage.gateway.api.common;

/**
 * @Author fanht
 * @Description
 * @Date 2020/2/26 下午5:55
 * @Version 1.0
 */
public enum AuthEnum {

    //超级管理员 可以管理多个商户
    SUPER_MANAGE(1000),
    //管理员
    MANAGE(900),
    //普通用户
    COMMON(100);

    AuthEnum(Integer authId) {
        this.authId = authId;
    }

    private Integer authId;

    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }
}
