package com.sq.transportmanage.gateway.service.common.enums;

/***
* @Description: 数据权限级别
* @Author: zjw
* @Date: 2020/4/29
*/
public enum DataLevelEnum {

//    数据权限 1 运力商 2 城市 3 车队 4 班组 0 无
    INIT_LEVEL(0,"初始无"),
    SUPPLIER_LEVEL(1,"运力商"),
    CITY_LEVEL(2,"城市"),
    TEAM_LEVEL(4,"车队"),
    GROUP_LEVEL(8,"班组");

    private Integer code;

    private String msg;

    DataLevelEnum(Integer code, String msg) {
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
