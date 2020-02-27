package com.sq.transportmanage.gateway.api.common;

/**
 * @Author fanht
 * @Description
 * @Date 2020/2/26 下午5:55
 * @Version 1.0
 */
public enum AuthEnum {

    MANAGE(900),
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