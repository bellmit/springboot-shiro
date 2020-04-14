package com.sq.transportmanage.gateway.service.common.enums;

/**
 * @Author fanht
 * @Description
 * @Date 2020/4/13 下午6:00
 * @Version 1.0
 */
public enum TeamType {

    TEAM(1,"车队"),
    GROUP(2,"班组");

    private Integer code;

    private String msg;

    TeamType(Integer code, String msg) {
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
